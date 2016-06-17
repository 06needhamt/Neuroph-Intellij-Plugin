package com.thomas.needham.neurophidea.designer.editor

import com.intellij.openapi.fileTypes.FileTypeEvent
import com.intellij.openapi.fileTypes.FileTypeListener

/**
 * Created by thoma on 17/06/2016.
 */
class NnetFileTypeListener : FileTypeListener.Adapter {
    val component: NnetEditorComponent
    constructor(component : NnetEditorComponent) : super(){
        this.component = component
    }
    override fun fileTypesChanged(event : FileTypeEvent) {
        super.fileTypesChanged(event)
        NnetEditorComponent.assertThread()
        component.updateValidProperty()
        component.updateHighlighters()
    }
}