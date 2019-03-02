/*
The MIT License (MIT)

Copyright (c) 2016 Tom Needham

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.thomas.needham.neurophidea.designer.editor.snnet

import com.intellij.AppTopics
import com.intellij.CommonBundle
import com.intellij.codeStyle.CodeStyleFacade
import com.intellij.diff.DiffContentFactory
import com.intellij.diff.DiffManager
import com.intellij.diff.requests.SimpleDiffRequest
import com.intellij.diff.util.DiffUserDataKeys
import com.intellij.openapi.application.AccessToken
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.TransactionGuard
import com.intellij.openapi.application.TransactionGuardImpl
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentAdapter
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.ex.DocumentEx
import com.intellij.openapi.editor.impl.EditorFactoryImpl
import com.intellij.openapi.editor.impl.TrailingSpacesStripper
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.fileEditor.FileDocumentSynchronizationVetoer
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.LoadTextUtil
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.UnknownFileType
import com.intellij.openapi.project.*
import com.intellij.openapi.project.ex.ProjectEx
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.Comparing
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.io.FileUtilRt
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.*
import com.intellij.openapi.vfs.newvfs.NewVirtualFileSystem
import com.intellij.pom.core.impl.PomModelImpl
import com.intellij.psi.ExternalChangeAction
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.SingleRootFileViewProvider
import com.intellij.testFramework.LightVirtualFile
import com.intellij.ui.UIBundle
import com.intellij.ui.components.JBScrollPane
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.messages.MessageBus
import com.thomas.needham.neurophidea.Constants
import com.thomas.needham.neurophidea.Predicates
import com.thomas.needham.neurophidea.exceptions.SaveVetoException
import org.jetbrains.annotations.NotNull
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.io.IOException
import java.lang.reflect.InvocationHandler
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*
import javax.swing.*

/**
 * Created by thoma on 24/06/2016.
 */
class SnnetDocumentManager : FileDocumentManager, VirtualFileListener, ProjectManagerListener, SafeWriteRequestor {
	val unsavedDocuments: MutableSet<Document?> = ContainerUtil.newConcurrentSet<Document?>()
	val messageBus: MessageBus?
	val documentManagerListener: FileDocumentManagerListener?
	val virtualFileManager: VirtualFileManager?
	val projectManager: ProjectManager?
	val trailingSpaceStripper = TrailingSpacesStripper()
	val documentCache: MutableMap<VirtualFile, Document>?
	var onClose = false;
	
	companion object Data {
		@JvmStatic
		val LOG = Logger.getInstance("#com.thomas.needham.neurophidea.designer.editor")
		@JvmStatic
		val HARD_REF_TO_DOCUMENT_KEY: Key<Document> = Key.create<Document>(Constants.DOCUMENT_HARD_REF_KEY)
		@JvmStatic
		val LINE_SEPARATOR_KEY: Key<String> = Key.create<String>(Constants.LINE_KEY)
		@JvmStatic
		val FILE_KEY: Key<VirtualFile> = Key.create<VirtualFile>(Constants.FILE_KEY);
		@JvmStatic
		val RECOMPUTE_FILE_TYPE = Key.create<Boolean>(Constants.RECOMPUTE_FILE_TYPE)
		@JvmStatic
		val LOCK: Any? = Any()
		
		@JvmName("getInstanceKt")
		@JvmStatic
		fun getInstance(): FileDocumentManager {
			return ApplicationManager.getApplication().getComponent(SnnetDocumentManager::class.java)
		}
		
		@JvmStatic
		fun unwrapAndRethrow(e: Exception) {
			var unpacked: Throwable = e
			if (e is InvocationTargetException) {
				if (e.cause == null) unpacked = e else unpacked = unpacked.cause!!
			}
			when (unpacked) {
				is Error -> throw unpacked
				is RuntimeException -> throw unpacked
				else -> {
					System.err.println(unpacked.message); unpacked.printStackTrace(System.err)
				}
			}
			LOG.error(unpacked)
		}
		
		@JvmStatic
		fun areTooManyDocumentsInTheQueue(documents: MutableSet<Document?>): Boolean {
			if (Predicates.MoreThan(documents.size, 200)) return true
			var totalSize: Int = 0
			for (doc: Document? in documents) {
				totalSize += doc?.textLength!!.toInt()
				if (Predicates.MoreThan(totalSize, FileUtilRt.LARGE_FOR_CONTENT_LOADING))
					return true
			}
			return false
		}
		
		@JvmStatic
		fun createDocument(text: CharSequence, file: VirtualFile): Document {
			val acceptSlash = file is LightVirtualFile && StringUtil.indexOf(text, '\r') >= 0
			val freeThreaded = file.getUserData(SingleRootFileViewProvider.FREE_THREADED) == true
			return (EditorFactory.getInstance() as EditorFactoryImpl).createDocument(text, acceptSlash, freeThreaded)
		}
		
		@JvmStatic
		fun isSaveNeeded(doc: Document, file: VirtualFile): Boolean {
			if (file.fileType.isBinary || Predicates.MoreThan(doc.textLength, (1000 * 1000))) {
				return true
			}
			val bytes = byteArrayOf(*file.contentsToByteArray())
			val loaded = LoadTextUtil.getTextByBinaryPresentation(bytes, file, false, false)
			return !(Comparing.equal(doc.charsSequence, loaded))
		}
		
		@JvmStatic
		fun needsRefresh(file: VirtualFile): Boolean {
			val vfs = file.fileSystem
			return vfs is NewVirtualFileSystem && file.timeStamp != vfs.getTimeStamp(file)
		}
		
		@JvmStatic
		fun updateModifiedProperty(file: VirtualFile) {
			for (project: Project? in ProjectManager.getInstance().openProjects) {
				val fileEditorManager: FileEditorManager? = FileEditorManager.getInstance(project!!)
				for (i in 0..fileEditorManager?.getAllEditors(file)?.size!! step 1) {
					if (fileEditorManager?.allEditors!![i] is SnnetEditorImpl) {
						(fileEditorManager?.allEditors!![i] as SnnetEditorImpl).updateModifiedProperty()
					}
				}
			}
		}
		
		@JvmStatic
		fun recomputeFileTypeIfNeccessary(@NotNull file: VirtualFile): Boolean {
			if (file.getUserData(RECOMPUTE_FILE_TYPE) != null) {
				file.fileType
				file.putUserData(RECOMPUTE_FILE_TYPE, null)
				return true
			}
			return false
		}
	}
	
	@Deprecated("DO NOT USE!!", ReplaceWith("constructor(@NotNull virtualFileManager : VirtualFileManager?, @NotNull projectManager : ProjectManager?)", *arrayOf("")), DeprecationLevel.ERROR)
	private constructor() : super() {
		this.virtualFileManager = null
		this.documentManagerListener = null
		this.projectManager = null
		this.documentCache = null
		this.messageBus = null
	}
	
	constructor(@NotNull virtualFileManager: VirtualFileManager?, @NotNull projectManager: ProjectManager?) : super() {
		this.virtualFileManager = virtualFileManager
		this.virtualFileManager?.addVirtualFileListener(this)
		this.projectManager = projectManager
		this.projectManager?.addProjectManagerListener(this)
		this.documentCache = ContainerUtil.createConcurrentWeakValueMap()
		this.messageBus = ApplicationManager.getApplication().messageBus
		val handler = SnnetInvocationHandler(this)
		val classLoader: ClassLoader = FileDocumentManagerListener::class.java.classLoader
		this.documentManagerListener = (Proxy.newProxyInstance(classLoader,
				arrayOf<Class<*>>(FileDocumentManagerListener::class.java as Class<*>) as Array<out Class<*>>,
				handler as InvocationHandler?)) as FileDocumentManagerListener?
	}
	
	override fun getFile(@NotNull p0: Document): VirtualFile? {
		return p0.getUserData(FILE_KEY)
	}
	
	override fun isPartialPreviewOfALargeFile(p0: Document): Boolean {
		return false
	}
	
	override fun saveAllDocuments() {
		saveAllDocuments(true)
	}
	
	fun saveAllDocuments(b: Boolean) {
		ApplicationManager.getApplication().assertIsDispatchThread()
		(TransactionGuard.getInstance() as TransactionGuardImpl).assertWriteActionAllowed()
		documentManagerListener?.beforeAllDocumentsSaving()
		if (unsavedDocuments.isEmpty()) return
		val failedToSaveMap: MutableMap<Document, IOException> = HashMap<Document, IOException>()
		val vector: MutableSet<Document> = HashSet<Document>()
		while (true) {
			var count = 0
			for (doc: Document? in unsavedDocuments) {
				if (failedToSaveMap.containsKey(doc)) continue
				if (vector.contains(doc)) continue
				try {
					doSaveDocument(doc, b)
				} catch (ioe: IOException) {
					ioe.printStackTrace(System.err)
					failedToSaveMap.put(doc!!, ioe)
				} catch (sve: SaveVetoException) {
					vector.add(doc!!)
				}
				count++
			}
			if (count == 0) break
		}
		if (!failedToSaveMap.isEmpty()) {
			handleErrorsOnSave(failedToSaveMap)
		}
	}
	
	private fun handleErrorsOnSave(failedToSaveMap: MutableMap<Document, IOException>) {
		if (ApplicationManager.getApplication().isUnitTestMode) {
			val ioe: IOException? = ContainerUtil.getFirstItem(ArrayList<IOException>(failedToSaveMap.values))
			if (ioe != null) {
				throw RuntimeException(ioe)
			}
			return
		}
		for (e: IOException? in failedToSaveMap.values) {
			LOG.warn(e as Throwable)
		}
		val text: String = StringUtil.join(failedToSaveMap.values, { ex: IOException ->
			ex.message
		}, "\n")
		val dialog = object : DialogWrapper(null) {
			override fun createDefaultActions() {
				super.createDefaultActions()
				myOKAction.putValue(Action.NAME, UIBundle.message(
						if (onClose) "cannot.save.files.dialog.ignore.changes" else "cannot.save.files.dialog.revert.changes"))
				myOKAction.putValue(DEFAULT_ACTION, null)
				if (!onClose) {
					myCancelAction.putValue(Action.NAME, CommonBundle.getCloseButtonText())
				}
			}
			
			override fun createCenterPanel(): JComponent? {
				val panel: JPanel = JPanel(BorderLayout(0, 5))
				panel.add(JLabel(UIBundle.message("cannot.save.files.dialog.message")), BorderLayout.NORTH)
				val area: JTextPane? = JTextPane()
				area?.text = text
				area?.isEditable = false
				area?.minimumSize = Dimension(area?.minimumSize?.width!!, 50)
				panel.add(JBScrollPane(area, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
						ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER)
						, BorderLayout.CENTER)
				return panel
			}
			
		};
		if (dialog.showAndGet()) {
			for (doc: Document? in failedToSaveMap.keys) {
				reloadFromDisk(doc!!)
			}
		}
	}
	
	private fun doSaveDocument(doc: Document?, b: Boolean) {
		val file = getFile(doc!!)
		if (file == null || file is LightVirtualFile || (file.isValid && !isFileModified(file))) {
			removeFromUnsaved(doc)
			return
		}
		if (file.isValid && needsRefresh(file)) {
			file.refresh(false, false)
			if (!unsavedDocuments.contains(doc)) return
		}
		for (vector: FileDocumentSynchronizationVetoer? in Extensions.getExtensions(FileDocumentSynchronizationVetoer.EP_NAME)) {
			if (!vector?.maySaveDocument(doc, b)!!)
				throw SaveVetoException()
		}
		val token: AccessToken? = ApplicationManager.getApplication().acquireWriteActionLock(SnnetDocumentManager::class.java)
		try {
			doSaveDocumentInWriteAction(doc, file)
		} finally {
			token?.finish()
		}
	}
	
	private fun doSaveDocumentInWriteAction(doc: Document, file: VirtualFile) {
		if (!file.isValid) {
			removeFromUnsaved(doc)
			return
		}
		if (!file.equals(getFile(doc))) {
			registerDocument(doc as DocumentEx, file as LightVirtualFile)
		}
		if (!isSaveNeeded(doc, file)) {
			if (doc is DocumentEx) {
				doc.modificationStamp = file.modificationStamp
			}
			removeFromUnsaved(doc)
			updateModifiedProperty(file)
			return
		}
		PomModelImpl.guardPsiModificationsIn<Exception> {
			documentManagerListener?.beforeDocumentSaving(doc)
			LOG.assertTrue(file.isValid)
			var text = doc.text
			val lineSeperator = "\n"
			if (lineSeperator.equals("\n")) {
				text = StringUtil.convertLineSeparators(text, lineSeperator)
			}
			val project: Project? = ProjectLocator.getInstance().guessProjectForFile(file)
			LoadTextUtil.write(project, file, this, text, doc.modificationStamp)
			unsavedDocuments.remove(doc)
			LOG.assertTrue(!unsavedDocuments.contains(doc))
			trailingSpaceStripper.clearLineModificationFlags(doc)
		}
	}
	
	private fun removeFromUnsaved(doc: Document) {
		unsavedDocuments.remove(doc)
		fireUnsavedDocumentsDropped()
		LOG.assertTrue(!unsavedDocuments.contains(doc))
	}
	
	private fun fireUnsavedDocumentsDropped() {
		documentManagerListener?.unsavedDocumentsDropped()
	}
	
	override fun getUnsavedDocuments(): Array<out Document> {
		if (unsavedDocuments.isEmpty()) {
			return Document.EMPTY_ARRAY
		}
		val list: MutableList<Document> = ArrayList<Document>(unsavedDocuments)
		return list.toTypedArray()
	}
	
	override fun isDocumentUnsaved(p0: Document): Boolean {
		return unsavedDocuments.contains(p0)
	}
	
	override fun reloadFiles(vararg p0: VirtualFile?) {
		for (file: VirtualFile? in p0) {
			if (file?.exists()!!) {
				val doc: Document? = getCachedDocument(file!!)
				if (doc != null) {
					reloadFromDisk(doc)
				}
			}
		}
	}
	
	override fun saveDocument(p0: Document) {
		saveDocument(p0, true)
	}
	
	fun saveDocument(document: Document, explicit: Boolean) {
		ApplicationManager.getApplication().assertIsDispatchThread()
		(TransactionGuard.getInstance() as TransactionGuardImpl).assertWriteActionAllowed()
		if (!unsavedDocuments.contains(document)) return
		try {
			doSaveDocument(document, explicit)
		} catch (ioe: IOException) {
			ioe.printStackTrace(System.err)
			handleErrorsOnSave(Collections.singletonMap(document, ioe))
		} catch (sve: SaveVetoException) {
			sve.printStackTrace(System.err)
		}
	}
	
	override fun requestWriting(p0: Document, p1: Project?): Boolean {
		val file: VirtualFile? = getInstance().getFile(p0)
		if (p1 != null && file != null && file.isValid) {
			return !(file.fileType.isBinary && ReadonlyStatusHandler.ensureFilesWritable(p1, file))
		}
		if (p0.isWritable) {
			return true
		}
		p0.fireReadOnlyModificationAttempt()
		return false
	}
	
	override fun isFileModified(p0: VirtualFile): Boolean {
		val doc: Document? = getCachedDocument(p0)
		return doc != null && isDocumentUnsaved(doc) && doc.modificationStamp != p0.modificationStamp
	}
	
	override fun getCachedDocument(@NotNull p0: VirtualFile): Document? {
		val hard: Document? = p0.getUserData(HARD_REF_TO_DOCUMENT_KEY)
		return hard ?: getDocumentFromCache(p0)
	}
	
	private fun getDocumentFromCache(p0: VirtualFile): Document? {
		return documentCache?.get(p0)
	}
	
	override fun reloadFromDisk(p0: Document) {
		ApplicationManager.getApplication().assertIsDispatchThread()
		val file: VirtualFile = getFile(p0)!!
		if (!fireBeforeFileContentReload(file, p0)) return
		if (file.length > FileUtilRt.LARGE_FOR_CONTENT_LOADING) {
			unbindFileFromDocument(file, p0)
			unsavedDocuments.remove(p0)
			documentManagerListener?.fileWithNoDocumentChanged(file)
			return
		}
		val project = ProjectLocator.getInstance().guessProjectForFile(file)
		CommandProcessor.getInstance().executeCommand(project, (object : Runnable {
			override fun run() {
				val wasWritable = p0.isWritable
				val docEx = p0 as DocumentEx
				docEx.setReadOnly(false)
				docEx.replaceText(LoadTextUtil.loadText(file), file.modificationStamp)
				docEx.setReadOnly(!wasWritable)
			}
			
		}), UIBundle.message("file.cache.conflict.action"), null, UndoConfirmationPolicy.REQUEST_CONFIRMATION)
		unsavedDocuments.remove(p0)
		documentManagerListener?.fileContentReloaded(file, p0)
	}
	
	private fun unbindFileFromDocument(file: VirtualFile?, p0: Document) {
		removeDocumentFromCache(file!!)
		file.putUserData(HARD_REF_TO_DOCUMENT_KEY, null)
		p0.putUserData(FILE_KEY, null)
	}
	
	private fun fireBeforeFileContentReload(file: VirtualFile, p0: Document): Boolean {
		for (vector: FileDocumentSynchronizationVetoer in Extensions.getExtensions(FileDocumentSynchronizationVetoer.EP_NAME)) {
			try {
				if (!vector.mayReloadFileContent(file, p0))
					return false
			} catch (e: Exception) {
				e.printStackTrace(System.err)
				LOG.error(e)
			}
		}
		documentManagerListener?.beforeFileContentReload(file, p0)
		return true
	}
	
	override fun saveDocumentAsIs(p0: Document) {
		val file: VirtualFile? = getFile(p0)
		var spaceStrippingEnabled = true
		if (file != null) {
			spaceStrippingEnabled = TrailingSpacesStripper.isEnabled(file)
			TrailingSpacesStripper.setEnabled(file, false)
		}
		try {
			saveDocument(p0)
		} finally {
			if (file != null) {
				TrailingSpacesStripper.setEnabled(file, spaceStrippingEnabled)
			}
		}
	}
	
	override fun getLineSeparator(p0: VirtualFile?, p1: Project?): String {
		return "\n"
	}
	
	override fun getDocument(p0: VirtualFile): Document? {
		ApplicationManager.getApplication().assertReadAccessAllowed();
		var document = getCachedDocument(p0) as DocumentEx?;
		if (!p0.isValid || p0.isDirectory || SingleRootFileViewProvider.isTooLargeForContentLoading(p0)) {
			return null
		} else {
			val text: CharSequence = LoadTextUtil.loadText(p0)
			synchronized(LOCK!!) {
				document = getCachedDocument(p0) as DocumentEx?
				if (document != null) return document
				document = createDocument(text, p0) as DocumentEx
				document?.modificationStamp = p0.modificationStamp
				val type: FileType = p0.fileType
				document?.setReadOnly(!p0.isWritable)
				if (p0 is LightVirtualFile) {
					registerDocument(document!!, p0)
				} else {
					cacheDocument(p0, document!!)
					document?.putUserData(FILE_KEY, p0)
				}
				if (!(p0 is LightVirtualFile || p0.fileSystem is NonPhysicalFileSystem)) {
					document?.addDocumentListener(object : DocumentAdapter() {
						override fun documentChanged(e: DocumentEvent) {
							val doc: Document? = e.document
							unsavedDocuments.add(doc)
							val command: Runnable? = CommandProcessor.getInstance().currentCommand
							val project: Project? = if (command == null) null else CommandProcessor.getInstance().currentCommandProject
							val lineSeperator = CodeStyleFacade.getInstance(project).lineSeparator
							if (areTooManyDocumentsInTheQueue(unsavedDocuments)) {
								saveAllDocumentsLater()
							}
						}
					})
				}
			}
			documentManagerListener?.fileContentLoaded(p0, document!!)
		}
		return document
	}
	
	fun saveAllDocumentsLater() {
		ApplicationManager.getApplication().invokeLater(object : Runnable {
			override fun run() {
				if (ApplicationManager.getApplication().isDisposed)
					exit@ Unit
				
				val unsavedDocuments: Array<out Document?> = getUnsavedDocuments()
				for (doc: Document? in unsavedDocuments) {
					val file: VirtualFile? = getFile(doc!!)
					if (file == null) continue
					val project: Project? = guessProjectForFile(file)
					if (project == null) continue
					if (PsiDocumentManager.getInstance(project).isDocumentBlockedByPsi(doc)) {
						saveDocument(doc)
					}
				}
			}
		})
	}
	
	fun cacheDocument(file: VirtualFile, document: DocumentEx) {
		documentCache?.put(file, document)
	}
	
	fun registerDocument(document: DocumentEx, file: LightVirtualFile) {
		synchronized(LOCK!!, {
			file.putUserData(HARD_REF_TO_DOCUMENT_KEY, document)
			document.putUserData(FILE_KEY, file)
		})
	}
	
	override fun beforePropertyChange(p0: VirtualFilePropertyEvent) {
	
	}
	
	override fun beforeContentsChange(p0: VirtualFileEvent) {
		val file: VirtualFile? = p0.file
		if (file?.length == 0L && file?.fileType == UnknownFileType.INSTANCE) {
			file?.putUserData(RECOMPUTE_FILE_TYPE, true)
			
		}
	}
	
	override fun fileDeleted(p0: VirtualFileEvent) {
		val doc: Document? = getCachedDocument(p0.file)
		if (doc != null) {
			trailingSpaceStripper.documentDeleted(doc)
		}
	}
	
	override fun beforeFileMovement(p0: VirtualFileMoveEvent) {
	}
	
	override fun fileMoved(p0: VirtualFileMoveEvent) {
	
	}
	
	override fun propertyChanged(p0: VirtualFilePropertyEvent) {
		val file: VirtualFile? = p0.file
		if (VirtualFile.PROP_WRITABLE.equals(p0.propertyName)) {
			val doc: Document? = getCachedDocument(file!!)
			if (doc != null) {
				ApplicationManager.getApplication().runWriteAction(object : ExternalChangeAction {
					override fun run() {
						doc.setReadOnly(!file.isWritable)
					}
					
				})
			} else if (VirtualFile.PROP_NAME.equals(p0.propertyName)) {
				val doc: Document? = getCachedDocument(file)
				if (doc != null) {
					println("Found Unknown File Type: ${file.fileType.name}")
				}
			}
		}
	}
	
	override fun contentsChanged(p0: VirtualFileEvent) {
		if (p0.isFromSave) return
		val file: VirtualFile? = p0.file
		val doc: Document? = getCachedDocument(file!!)
		if (doc == null) {
			documentManagerListener?.fileWithNoDocumentChanged(file)
			return
		}
		val documentStamp = doc.modificationStamp
		val oldDocumentStamp = p0.oldModificationStamp
		if (documentStamp != oldDocumentStamp) {
			LOG.info("reload " + file.getName() + " from disk?");
			LOG.info("  documentStamp:" + documentStamp);
			LOG.info("  oldFileStamp:" + oldDocumentStamp);
			if (file.isValid && askReloadFromDisk(file, doc)) {
				reloadFromDisk(doc)
			}
		} else {
			reloadFromDisk(doc)
		}
	}
	
	private fun askReloadFromDisk(file: VirtualFile, doc: Document): Boolean {
		ApplicationManager.getApplication().assertIsDispatchThread()
		if (!isDocumentUnsaved(doc)) return true
		return askReloadFromDisk.invoke(file, doc)
	}
	
	override fun beforeFileDeletion(p0: VirtualFileEvent) {
	
	}
	
	override fun fileCreated(p0: VirtualFileEvent) {
	
	}
	
	override fun fileCopied(p0: VirtualFileCopyEvent) {
		fileCreated(p0 as VirtualFileEvent)
	}
	
	override fun canCloseProject(p0: Project): Boolean {
		if (!unsavedDocuments.isEmpty()) {
			onClose = true
			try {
				saveAllDocuments()
			} finally {
				onClose = false
			}
		}
		return unsavedDocuments.isEmpty()
	}
	
	override fun projectClosing(p0: Project) {
	}
	
	override fun projectClosed(p0: Project) {
	}
	
	override fun projectOpened(p0: Project) {
	}
	
	@SuppressWarnings("OverlyBroadCatchBlock")
	fun multiCast(method: Method?, args: Array<out Any>?) {
		val arguments = ParseArguments(args)
		try {
			if (method?.parameterCount == 1)
				method?.invoke(messageBus?.syncPublisher(AppTopics.FILE_DOCUMENT_SYNC), arguments[0] as VirtualFile?)
			else if (method?.parameterCount == 2)
				method?.invoke(messageBus?.syncPublisher(AppTopics.FILE_DOCUMENT_SYNC), arguments[0] as VirtualFile?, arguments[1] as Document?)
			else
				method?.invoke(messageBus?.syncPublisher(AppTopics.FILE_DOCUMENT_SYNC), *arguments)
			
		} catch (cce: ClassCastException) {
			LOG.error("Arguments ${Arrays.toString(args)}", cce)
		} catch (e: Exception) {
			unwrapAndRethrow(e) //uncomment when debugging
			e.printStackTrace(System.err)
		}
		for (listener: FileDocumentManagerListener? in getListeners()) {
			try {
				method?.invoke(listener, arguments)
			} catch (e: Exception) {
				unwrapAndRethrow(e) //uncomment when debugging
				e.printStackTrace(System.err)
			}
		}
		try {
			if (method?.parameterCount == 1)
				method?.invoke(messageBus?.syncPublisher(AppTopics.FILE_DOCUMENT_SYNC), arguments[0] as VirtualFile?)
			else if (method?.parameterCount == 2)
				method?.invoke(messageBus?.syncPublisher(AppTopics.FILE_DOCUMENT_SYNC), arguments[0] as VirtualFile?, arguments[1] as Document?)
			else
				method?.invoke(messageBus?.syncPublisher(AppTopics.FILE_DOCUMENT_SYNC), *arguments)
		} catch (e: Exception) {
			unwrapAndRethrow(e) //uncomment when debugging
			e.printStackTrace(System.err)
		}
	}
	
	private fun ParseArguments(args: Array<out Any>?): Array<Any?> {
		val init: (Int) -> Any? = {
			Any()
		}
		val arguments = Array<Any?>(args?.size!!, init)
		for (i in 0..args?.size!! - 1) {
			if (i == 0) {
				arguments[i] = args!![i] as VirtualFile?
			} else {
				arguments[i] = args!![i]
			}
		}
		return arguments
	}
	
	private fun getListeners(): Array<out FileDocumentManagerListener?> {
		return FileDocumentManagerListener.EP_NAME.extensions
	}
	
	@Volatile
	private var askReloadFromDisk = { file: VirtualFile, document: Document ->
		val message = UIBundle.message("file.cache.conflict.message.text", file.getPresentableUrl())
		
		val builder = DialogBuilder()
		builder.setCenterPanel(JLabel(message, Messages.getQuestionIcon(), SwingConstants.CENTER))
		builder.addOkAction().setText(UIBundle.message("file.cache.conflict.load.fs.changes.button"))
		builder.addCancelAction().setText(UIBundle.message("file.cache.conflict.keep.memory.changes.button"))
		builder.addAction(object : AbstractAction() {
			override fun actionPerformed(p0: ActionEvent?) {
				val project = ProjectLocator.getInstance().guessProjectForFile(file) as ProjectEx
				
				val fileType = file.getFileType()
				val fsContent = LoadTextUtil.loadText(file).toString()
				val content1 = DiffContentFactory.getInstance().create(fsContent, fileType)
				val content2 = DiffContentFactory.getInstance().create(project, document, file)
				val title = UIBundle.message("file.cache.conflict.for.file.dialog.title", file.getPresentableUrl())
				val title1 = UIBundle.message("file.cache.conflict.diff.content.file.system.content")
				val title2 = UIBundle.message("file.cache.conflict.diff.content.memory.content")
				val request = SimpleDiffRequest(title, content1, content2, title1, title2)
				request.putUserData(DiffUserDataKeys.GO_TO_SOURCE_DISABLE, true)
				val diffBuilder = DialogBuilder(project)
				val diffPanel = DiffManager.getInstance().createRequestPanel(project, diffBuilder, diffBuilder.getWindow())
				diffPanel.setRequest(request)
				diffBuilder.setCenterPanel(diffPanel.getComponent())
				diffBuilder.setDimensionServiceKey("FileDocumentManager.FileCacheConflict")
				diffBuilder.addOkAction().setText(UIBundle.message("file.cache.conflict.save.changes.button"))
				diffBuilder.addCancelAction()
				diffBuilder.setTitle(title)
				if (diffBuilder.show() === DialogWrapper.OK_EXIT_CODE) {
					builder.getDialogWrapper().close(DialogWrapper.CANCEL_EXIT_CODE)
				}
			}
		});
		builder.setTitle(UIBundle.message("file.cache.conflict.dialog.title"))
		builder.setButtonsAlignment(SwingConstants.CENTER)
		builder.setHelpId("reference.dialogs.fileCacheConflict")
		builder.show() == 0
	}
	
	fun removeDocumentFromCache(file: VirtualFile) {
		documentCache?.remove(file)
	}
}