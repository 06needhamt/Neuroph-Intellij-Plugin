package com.thomas.needham.neurophidea.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.thomas.needham.neurophidea.forms.create.CreateNetworkForm
import com.thomas.needham.neurophidea.forms.open.OpenNetworkForm

/**
 * Created by thoma on 13/06/2016.
 */
class OpenExistingNetworkAction : AnAction() {
    var form : OpenNetworkForm? = null
    var itr : Long = 0L

    companion object ProjectInfo{
        var project : Project? = null
        var projectDirectory : String? = ""
        var isOpen : Boolean? = false
    }
    override fun actionPerformed(e : AnActionEvent) {
        InitialisationAction.project = e.project
        InitialisationAction.projectDirectory = InitialisationAction.project?.basePath
        InitialisationAction.isOpen = InitialisationAction.project?.isOpen
        form = OpenNetworkForm()
    }
}
