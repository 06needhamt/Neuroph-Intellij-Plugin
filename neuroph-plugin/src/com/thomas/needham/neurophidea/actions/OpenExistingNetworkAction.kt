package com.thomas.needham.neurophidea.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.thomas.needham.neurophidea.actions.InitAction

/**
 * Created by thoma on 27/05/2016.
 */
class OpenExistingNetworkAction : AnAction() {

    companion object Data {
        var project : Project? = null
        var projectDirectory : String? = ""
        var isOpen : Boolean? = false
        var fileDesc : FileChooserDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
    }
    override fun actionPerformed(e : AnActionEvent) {
        project = InitAction.project
        projectDirectory = InitAction.projectDirectory
        isOpen = project?.isOpen

    }
}
