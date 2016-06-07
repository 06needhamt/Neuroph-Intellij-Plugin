package com.thomas.needham.neurophidea.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileChooserFactory
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.consumers.NeurophSDKConsumer
import com.thomas.needham.neurophidea.project.NeurophSDKPanel

/**
 * Created by thoma on 06/06/2016.
 */
class DownloadNeurophAction : AnAction() {
    companion object Data {
        @JvmStatic var panel : NeurophSDKPanel? = null
        var consumer : NeurophSDKConsumer? = NeurophSDKConsumer()
    }

    override fun actionPerformed(anActionEvent : AnActionEvent) {
        val descriptor = FileChooserDescriptor(false, true, false, false, false, false)
        val pathChooser = FileChooserFactory.getInstance().createPathChooser(descriptor, null, panel)
        pathChooser.choose(VfsUtil.getUserHomeDir(), consumer as Consumer<MutableList<VirtualFile>> )
    }
}
