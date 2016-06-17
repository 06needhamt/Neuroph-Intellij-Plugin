package com.thomas.needham.neurophidea.designer.editor

import com.intellij.openapi.project.DumbService

/**
 * Created by thoma on 17/06/2016.
 */
class NnetDumbModeListener : DumbService.DumbModeListener {
    val component : NnetEditorComponent
    constructor(component : NnetEditorComponent){
        this.component = component
    }
    override fun enteredDumbMode() {
        component.updateHighlighters()
    }

    override fun exitDumbMode() {
        component.updateHighlighters()
    }
}