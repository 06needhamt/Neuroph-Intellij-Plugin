/*
    The MIT License (MIT)
    
    neuroph-plugin Copyright (c) 2016 thoma
    
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

package com.thomas.needham.neurophidea.core

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFileCopyEvent
import com.intellij.openapi.vfs.VirtualFileEvent
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileMoveEvent
import com.intellij.openapi.vfs.VirtualFilePropertyEvent
import com.thomas.needham.neurophidea.PluginRegistration
import com.thomas.needham.neurophidea.actions.AutoGenerateCodeAction
import java.awt.event.InputEvent

/**
 * Created by thoma on 26/09/2016.
 */
class FileSaveListener : VirtualFileListener {
    override fun beforePropertyChange(p0 : VirtualFilePropertyEvent) {

    }

    override fun beforeContentsChange(p0 : VirtualFileEvent) {

    }

    override fun fileDeleted(p0 : VirtualFileEvent) {

    }

    override fun beforeFileMovement(p0 : VirtualFileMoveEvent) {

    }

    override fun fileMoved(p0 : VirtualFileMoveEvent) {

    }

    override fun propertyChanged(p0 : VirtualFilePropertyEvent) {

    }

    override fun contentsChanged(p0 : VirtualFileEvent) {
        val generator : AutoCodeGenerator = AutoCodeGenerator()
        if(!generator.GenerateCode() && generator.error && !PluginRegistration.projectClosing)
            Messages.showErrorDialog(AutoCodeGenerator.project, "Error Generating Code", "Error")
    }

    override fun beforeFileDeletion(p0 : VirtualFileEvent) {

    }

    override fun fileCreated(p0 : VirtualFileEvent) {

    }

    override fun fileCopied(p0 : VirtualFileCopyEvent) {

    }
}