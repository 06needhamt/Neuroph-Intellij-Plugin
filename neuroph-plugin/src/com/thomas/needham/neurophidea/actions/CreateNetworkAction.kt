package com.thomas.needham.neurophidea.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.thomas.needham.neurophidea.actions.InitAction
import com.thomas.needham.neurophidea.forms.CreateNetworkForm
import java.awt.Toolkit
import javax.swing.WindowConstants

/**
 * Created by thoma on 25/05/2016.
 */
class CreateNetworkAction : AnAction() {
    var form : CreateNetworkForm? = null
    var itr : Long = 0

    companion object ProjectInfo{
        var project : Project? = null
        var projectDirectory : String? = ""
        var isOpen : Boolean? = false
    }
    override fun actionPerformed(e : AnActionEvent) {
        InitAction.project = e.project
        InitAction.projectDirectory = InitAction.project?.basePath
        InitAction.isOpen = InitAction.project?.isOpen
        form = CreateNetworkForm()
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
