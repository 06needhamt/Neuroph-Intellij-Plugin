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

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.thomas.needham.neurophidea.forms.export.ExportNetworkForm
import com.thomas.needham.neurophidea.forms.train.TrainNetworkForm

/**
 * Created by thoma on 30/05/2016.
 */
class ShowTrainNetworkFormAction : AnAction() {
    var form : TrainNetworkForm? = null
    var itr : Long = 0L
    companion object Data{
        var project : Project? = null
        var projectDirectory : String? = ""
        var isOpen : Boolean? = false
        val properties = PropertiesComponent.getInstance()

    }
    override fun actionPerformed(e : AnActionEvent) {
        InitialisationAction.project = e.project
        InitialisationAction.projectDirectory = InitialisationAction.project?.basePath
        InitialisationAction.isOpen = InitialisationAction.project?.isOpen
        form = TrainNetworkForm()
    }

    override fun update(e : AnActionEvent?) {
        super.update(e)
        if(form != null){
            form?.repaint(itr,0,0,form?.width!!,form?.height!!)
            itr++
        }
        e?.presentation?.isEnabledAndVisible = true
    }
}
