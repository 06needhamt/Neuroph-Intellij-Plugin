package com.thomas.needham.neurophidea.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * Created by thoma on 23/09/2016.
 */
class AutoGenerateCodeAction : AnAction() {
	
	override fun actionPerformed(e: AnActionEvent) {
		println("Action Fired")
	}
}
