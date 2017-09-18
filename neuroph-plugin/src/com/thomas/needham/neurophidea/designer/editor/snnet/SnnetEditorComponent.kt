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

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.ex.EditorMarkupModel
import com.intellij.openapi.editor.highlighter.EditorHighlighter
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory
import com.intellij.openapi.editor.impl.EditorImpl
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager
import com.intellij.openapi.fileEditor.impl.text.FileDropHandler
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.WindowManager
import com.intellij.openapi.wm.ex.StatusBarEx
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.util.messages.MessageBusConnection
import org.jetbrains.annotations.NotNull
import java.awt.BorderLayout

/**
 * Created by thoma on 24/06/2016.
 */
class SnnetEditorComponent : JBLoadingPanel, DataProvider {
    val project : Project?
    @NotNull val file : VirtualFile?
    val snnetEditor : SnnetEditorImpl?
    val editor : Editor?
    val document : Document?
    val documentListener : SnnetDocumentListener?
    var isComponentModified : Boolean
    var isComponentValid : Boolean
    val fileListener : SnnetVirtualFileListener?
    val busConnection : MessageBusConnection?

    companion object Log {
        @JvmStatic val LOG : Logger = Logger.getInstance("#com.thomas.needham.neurophidea.designer.editor.snnet.SnnetEditorComponent")
        @JvmStatic val assertThread : () -> Unit = {
            ApplicationManager.getApplication().assertIsDispatchThread()
        }
    }

    constructor(@NotNull project : Project?, @NotNull file : VirtualFile?, @NotNull editor : SnnetEditorImpl?)
    : super(BorderLayout(), editor as Disposable) {
        this.project = project
        this.file = file
        this.snnetEditor = editor
        this.document = SnnetDocumentManager.getInstance().getDocument(file!!)
        LOG.assertTrue(this.document != null)
        documentListener = SnnetDocumentListener(this)
        document?.addDocumentListener(documentListener)
        this.editor = createEditor()
        this.add(this, BorderLayout.CENTER)
        isComponentModified = isModifiedImpl()
        isComponentValid = isEditorValidImpl()
        LOG.assertTrue(isComponentValid)
        fileListener = SnnetVirtualFileListener(this)
        this.file?.fileSystem?.addVirtualFileListener(fileListener)
        busConnection = project?.messageBus?.connect()
        busConnection?.subscribe(FileTypeManager.TOPIC, SnnetFileTypeListener(this))
        busConnection?.subscribe(DumbService.DUMB_MODE, SnnetDumbModeListener(this))
    }
        @NotNull
    private fun createEditor() : Editor? {
        val e : Editor? = EditorFactory.getInstance().createEditor(document!!, project)
        (e?.markupModel as EditorMarkupModel).isErrorStripeVisible = true
        (e as EditorEx).gutterComponentEx.setForceShowRightFreePaintersArea(true)
        e.setFile(file)
        e.contextMenuGroupId = IdeActions.GROUP_EDITOR_POPUP
        (e as EditorImpl).setDropHandler(FileDropHandler(e))
        SnnetFileEditorProvider.putSnnetEditor(e, snnetEditor)
        return e
    }
    private fun isEditorValidImpl() : Boolean {
        return SnnetDocumentManager.getInstance().getDocument(file!!) != null
    }

    override fun getData(p0 : String?) : Any? {
        throw UnsupportedOperationException()
    }

    fun updateModifiedProperty() {
        val oldModified = isComponentModified
        isComponentModified = isModifiedImpl()
        snnetEditor?.firePropertyChange(FileEditor.PROP_MODIFIED, oldModified, isComponentModified)
    }

    private fun isModifiedImpl() : Boolean {
        return SnnetDocumentManager.getInstance().isFileModified(file!!);
    }

    fun updateValidProperty() {
        val oldValid = isComponentValid
        isComponentValid = isEditorValidImpl()
        snnetEditor?.firePropertyChange(FileEditor.PROP_VALID, oldValid, isComponentValid)
    }

    fun updateHighlighters() {
        if (!project?.isDisposed!! && !editor?.isDisposed!!) {
            val highlighter : EditorHighlighter = EditorHighlighterFactory.getInstance().createEditorHighlighter(project, file!!)
            (editor as EditorEx).highlighter = highlighter
        }
    }

    fun dispose() {
        document?.removeDocumentListener(documentListener!!)
        if (!project?.isDefault!!) {
            EditorHistoryManager.getInstance(project!!).updateHistoryEntry(file, false)
        }
        disposeEditor()
        busConnection?.disconnect()
        file?.fileSystem?.removeVirtualFileListener(fileListener!!)
    }

    private fun disposeEditor() {
        EditorFactory.getInstance().releaseEditor(editor!!)
    }

    fun selectNotify() {
        updateStatusBar()
    }

    private fun updateStatusBar() {
        val statusBar : StatusBarEx? = WindowManager.getInstance().getStatusBar(project) as StatusBarEx
        statusBar?.updateWidgets()
    }
}