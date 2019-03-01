package com.thomas.needham.neurophidea.consumers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.intellij.util.download.DownloadableFileService
import com.intellij.util.io.ZipUtil
import com.thomas.needham.neurophidea.Constants.LAST_USED_NEUROPH_HOME
import com.thomas.needham.neurophidea.actions.DownloadNeurophAction
import com.thomas.needham.neurophidea.project.NeurophSDKPanel
import java.io.IOException
import java.util.*

/**
 * Created by Thomas Needham on 06/06/2016.
 */
class NeurophSDKConsumer : Consumer<MutableList<VirtualFile?>?> {
	override fun consume(virtualFiles: MutableList<VirtualFile?>?) {
		if (virtualFiles?.size == 1) {
			var dir = virtualFiles!![0]
			val dirName = dir?.name
			val accessToken = ApplicationManager.getApplication().acquireWriteActionLock(NeurophSDKPanel::class.java)
			try {
				if (!dirName?.toLowerCase()?.contains("smalltalk")!! && !dirName?.toLowerCase()?.contains("redline")!!) {
					try {
						dir = dir?.createChildDirectory(this, "Neuroph")
					} catch (e: IOException) {//
					}
					
				}
				val fileService = DownloadableFileService.getInstance()
				val fileDescription = fileService.createFileDescription("http://downloads.sourceforge.net/project/neuroph/neuroph%202.6/neuroph-2.6.zip?r=https%3A%2F%2Fsourceforge.net%2Fprojects%2Fneuroph%2Ffiles%2Fneuroph%25202.6%2F&ts=1465210537&use_mirror=liquidtelecom", "neuroph-2.6.jar")
				val downloader = fileService.createDownloader(Arrays.asList(fileDescription), null, DownloadNeurophAction.panel!!.root, "neuroph-2.6.jar")
				val files = downloader.toDirectory(dir?.path!!).download()
				if (files != null && files.size == 1) {
					try {
						ZipUtil.extract(VfsUtil.virtualToIoFile(files[0]), VfsUtil.virtualToIoFile(dir!!), null)
						PropertiesComponent.getInstance().setValue(LAST_USED_NEUROPH_HOME, dir.path)
					} catch (e: IOException) {
						e.printStackTrace()
					} finally {
						dir?.refresh(false, true)
					}
				}
			} finally {
				accessToken.finish()
			}
		}
	}
}
