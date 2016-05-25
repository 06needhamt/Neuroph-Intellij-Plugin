package com.thomas.needham.neurophidea

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.thomas.needham.neurophidea.forms.CreateNetworkForm
import java.awt.Toolkit
import javax.swing.WindowConstants

/**
 * Created by thoma on 25/05/2016.
 */
class CreateNetworkAction : AnAction() {

    override fun actionPerformed(e : AnActionEvent) {
        val form = CreateNetworkForm()
    }

    override fun update(e : AnActionEvent?) {
        super.update(e)
        e?.presentation?.isEnabledAndVisible = true
    }
}
