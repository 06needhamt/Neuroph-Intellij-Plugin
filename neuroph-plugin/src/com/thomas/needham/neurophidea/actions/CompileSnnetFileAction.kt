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


import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.PlatformIcons
import com.thomas.needham.neurophidea.designer.psi.snnet.generator.SnnetGenerator
import com.thomas.needham.neurophidea.designer.psi.snnet.lexer.SnnetLexer
import com.thomas.needham.neurophidea.designer.psi.snnet.parser.SnnetParser
import java.io.File

/**
 * Created by thoma on 29/06/2016.
 */
class CompileSnnetFileAction : AnAction() {

    companion object ProjectInfo{
        var project : Project? = null
        var projectDirectory : String? = ""
        var isOpen : Boolean? = false
    }

    override fun actionPerformed(p0 : AnActionEvent?) {
        project = p0?.project
        projectDirectory = project?.basePath
        isOpen = project?.isOpen
        val doc = FileEditorManager.getInstance(project!!).selectedTextEditor?.document
        val file = FileDocumentManager.getInstance().getFile(doc!!)
        val lexer = SnnetLexer(file)
        if(!lexer.Start()){
            Messages.showErrorDialog(project, "Error Compiling File ${file?.path}", "Error")
            return
        }
        val tokens = lexer.tokenList
        val parser = SnnetParser(tokens)
        if(!parser.Start()){
            Messages.showErrorDialog(project, "Error Parsing File ${file?.path}", "Error")
            return
        }
        val path = file?.path?.substring(0,file.path.length - file.name.length) + "Compiled.conf"
        val generator = SnnetGenerator(parser.properties)
        generator.SaveNetwork(path)
        Messages.showOkCancelDialog(project,"Network Successfully Compiled To ${path}", "Success",PlatformIcons.CHECK_ICON)
    }
}
