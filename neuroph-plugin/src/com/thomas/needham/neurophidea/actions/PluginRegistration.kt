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
package com.thomas.needham.neurophidea.actions

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.ApplicationComponent
import com.intellij.openapi.fileTypes.BinaryFileTypeDecompilers
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.fileTypes.impl.FileTypeManagerImpl
import com.thomas.needham.neurophidea.Constants.INITIALISATION_ACTION
import com.thomas.needham.neurophidea.Constants.MENU_ACTION
import com.thomas.needham.neurophidea.Constants.WINDOW_MENU_ACTION
import com.thomas.needham.neurophidea.designer.psi.DataSetFileType
import com.thomas.needham.neurophidea.designer.editor.nnet.NnetDecompiler
import com.thomas.needham.neurophidea.designer.psi.nnet.NnetFileType
import com.thomas.needham.neurophidea.designer.psi.snnet.SnnetFileType

/**
 * Created by Thomas Needham on 25/05/2016.
 */
class PluginRegistration : ApplicationComponent {
    val actionManager = ActionManager.getInstance()
    val init = InitialisationAction()
    val fileManager = FileTypeManagerImpl.getInstance()
    //val menu = MenuAction()

    override fun getComponentName() : String {
        return "Neuroph For Intellij"
    }

    override fun disposeComponent() {
        println("Plugin Unloaded")
        actionManager.unregisterAction(MENU_ACTION)
        actionManager.unregisterAction(INITIALISATION_ACTION)
    }

    override fun initComponent() {
        println("Plugin Loaded")
        fileManager.registerFileType(NnetFileType(),*arrayOf("nnet"))
        fileManager.registerFileType(SnnetFileType(),*arrayOf("snnet"))
        fileManager.registerFileType(DataSetFileType(),*arrayOf("tset"))
        BinaryFileTypeDecompilers.INSTANCE.addExplicitExtension(fileManager.getFileTypeByExtension("nnet"), NnetDecompiler())
        actionManager.registerAction(INITIALISATION_ACTION, init)
        //actionManager.registerAction(MENU_ACTION,menu)
        val defaultActionGroup : DefaultActionGroup? = actionManager.getAction(WINDOW_MENU_ACTION) as DefaultActionGroup?
        defaultActionGroup?.addSeparator()
        //defaultActionGroup?.addAction(menu)
    }
}