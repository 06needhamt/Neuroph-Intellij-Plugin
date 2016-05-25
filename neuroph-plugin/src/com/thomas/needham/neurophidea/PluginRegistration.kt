package com.thomas.needham.neurophidea

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.ApplicationComponent
import com.thomas.needham.neurophidea.Constants.INITIALISATION_ACTION
import com.thomas.needham.neurophidea.Constants.MENU_ACTION
import com.thomas.needham.neurophidea.Constants.WINDOW_MENU_ACTION

/**
 * Created by thoma on 25/05/2016.
 */
class PluginRegistration : ApplicationComponent {
    val actionManager = ActionManager.getInstance()
    val init = InitAction()
    //val menu = MenuAction()

    override fun getComponentName() : String {
        return "Neuroph For Intellij"
    }

    override fun disposeComponent() {
        println("Plugin Unloaded")
        actionManager.unregisterAction(MENU_ACTION)
        actionManager.unregisterAction(INITIALISATION_ACTION)
    }

    override fun initComponent() {
        println("Plugin Loaded")
        actionManager.registerAction(INITIALISATION_ACTION, init)
        //actionManager.registerAction(MENU_ACTION,menu)
        val defaultActionGroup : DefaultActionGroup? = actionManager.getAction(WINDOW_MENU_ACTION) as DefaultActionGroup?
        defaultActionGroup?.addSeparator()
        //defaultActionGroup?.addAction(menu)
    }
}