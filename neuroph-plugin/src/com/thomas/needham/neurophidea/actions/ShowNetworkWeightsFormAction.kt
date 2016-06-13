package com.thomas.needham.neurophidea.actions

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.thomas.needham.neurophidea.forms.output.NetworkOutputForm
import com.thomas.needham.neurophidea.forms.output.WindowCloseListener
import com.thomas.needham.neurophidea.forms.weights.NetworkWeightsForm
import org.neuroph.core.NeuralNetwork
import java.io.File

/**
 * Created by thoma on 13/06/2016.
 */
class ShowNetworkWeightsFormAction : AnAction() {
    var form : NetworkWeightsForm? = null
    var itr : Long = 0L

    companion object Data{
        var project : Project? = null
        var projectDirectory : String? = ""
        var isOpen : Boolean? = false
        val properties = PropertiesComponent.getInstance()
        val openAction : OpenExistingNetworkAction? = OpenExistingNetworkAction()
        var path : String? = ""
        var network : NeuralNetwork? = null
    }
    override fun actionPerformed(e : AnActionEvent) {
        project = e.project
        projectDirectory = project?.basePath
        isOpen = project?.isOpen
        openAction?.actionPerformed(e)
        openAction?.form?.shouldClose = true
        val listener : WindowCloseListener? = WindowCloseListener {
            network = openAction?.form?.network
            form = NetworkWeightsForm(openAction?.form?.txtNetworkToOpen?.text,network!!)
        }
        openAction?.form?.addWindowListener(listener)
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
