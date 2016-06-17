package com.thomas.needham.neurophidea.designer.editor

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.event.DocumentAdapter
import com.intellij.openapi.editor.event.DocumentEvent

/**
 * Created by thoma on 17/06/2016.
 */
class NnetDocumentListener : DocumentAdapter {
    val updateRunnable : Runnable
    var updateScheduled : Boolean = false
    val component : NnetEditorComponent
    private val lambda : (NnetEditorComponent) -> Unit = { component ->
        updateScheduled = false
        component.updateModifiedProperty()
    }
    constructor(component : NnetEditorComponent){
        this.component = component
        this.updateRunnable = lambda as Runnable;
    }

    override fun documentChanged(e : DocumentEvent?) {
        super.documentChanged(e)
        if(!updateScheduled){
            ApplicationManager.getApplication().invokeLater(updateRunnable)
            updateScheduled = true
        }
    }
}