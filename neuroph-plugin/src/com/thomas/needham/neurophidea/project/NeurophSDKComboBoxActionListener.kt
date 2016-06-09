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
package com.thomas.needham.neurophidea.project

import com.intellij.configurationStore.stateToElement
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.ui.ProjectJdksEditor
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by Thomas Needham on 06/06/2016.
 */
class NeurophSDKComboBoxActionListener : ActionListener {
    var instance : NeurophSDKComboBox? = null
    companion object Data{
        val project = ProjectManager.getInstance().defaultProject
    }
    override fun actionPerformed(e : ActionEvent?) {
        var sdk : Sdk? = instance?.getSelectedSdk()
        var editor = ProjectJdksEditor(sdk,NeurophSDKComboBoxActionListener.project,instance?.rootPane)
        editor.show()
        if(editor.isOK){
            sdk = editor.selectedJdk
            instance?.updateSDKList(sdk,false)
        }
    }
}