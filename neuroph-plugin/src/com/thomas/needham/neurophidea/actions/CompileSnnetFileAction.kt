package com.thomas.needham.neurophidea.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * Created by thoma on 29/06/2016.
 */
class CompileSnnetFileAction : AnAction() {
    override fun actionPerformed(e : AnActionEvent) {
        println("In Compile Action")
    }
}
