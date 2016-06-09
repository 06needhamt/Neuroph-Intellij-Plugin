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
import com.intellij.openapi.ui.Messages
import com.thomas.needham.neurophidea.forms.output.NetworkOutputForm
import com.thomas.needham.neurophidea.forms.output.WindowCloseListener
import com.thomas.needham.neurophidea.forms.test.TestNetworkForm
import java.io.File

/**
 * Created by thoma on 09/06/2016.
 */
class ShowNetworkOutputFormAction : AnAction() {
    var form : NetworkOutputForm? = null
    var itr : Long = 0L

    companion object Data{
        var project : Project? = null
        var projectDirectory : String? = ""
        var isOpen : Boolean? = false
        val properties = PropertiesComponent.getInstance()
        val testAction : ShowTestNetworkFormAction? = ShowTestNetworkFormAction()
        var path : String? = ""
        var lastModifiedFile : (String?) -> File? = { dirPath ->
            val dir = File(dirPath)
            val files = dir.listFiles()
            if (files == null || files.size == 0) {
                throw IllegalArgumentException("Invalid File Path")
            }

            var lastModifiedFile = files[0]
            for (i in 1..files.size - 1) {
                if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                    lastModifiedFile = files[i]
                }
            }
            lastModifiedFile

        }
    }
    override fun actionPerformed(e : AnActionEvent) {
        InitialisationAction.project = e.project
        InitialisationAction.projectDirectory = InitialisationAction.project?.basePath
        InitialisationAction.isOpen = InitialisationAction.project?.isOpen
        testAction?.actionPerformed(e)
        val listener : WindowCloseListener? = WindowCloseListener {
            try {
                val dirpath = testAction?.form?.txtOutputPath?.text
                val file = lastModifiedFile(dirpath)
                if (file == null)
                    return@WindowCloseListener
                form = NetworkOutputForm(path!!)
            }
            catch(iae: IllegalArgumentException){
                iae.printStackTrace(System.err)
                return@WindowCloseListener
            }
        }
        testAction?.form?.addWindowListener(listener)
        exit@ Unit
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
