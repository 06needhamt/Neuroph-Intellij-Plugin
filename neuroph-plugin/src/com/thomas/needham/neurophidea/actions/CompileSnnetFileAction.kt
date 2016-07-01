package com.thomas.needham.neurophidea.actions


import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.PlatformIcons
import com.thomas.needham.neurophidea.designer.psi.snnet.lexer.SnnetLexer
import com.thomas.needham.neurophidea.designer.psi.snnet.parser.SnnetParser

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
        projectDirectory = InitialisationAction.project?.basePath
        isOpen = InitialisationAction.project?.isOpen
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
        Messages.showOkCancelDialog(project, "File Successfully Parsed", "Success", PlatformIcons.CHECK_ICON)
    }
}
