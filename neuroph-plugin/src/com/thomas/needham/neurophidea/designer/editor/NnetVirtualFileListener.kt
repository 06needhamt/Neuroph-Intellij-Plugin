package com.thomas.needham.neurophidea.designer.editor

import com.intellij.openapi.util.Comparing
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileAdapter
import com.intellij.openapi.vfs.VirtualFileEvent
import com.intellij.openapi.vfs.VirtualFilePropertyEvent
import com.intellij.util.FileContentUtilCore

/**
 * Created by thoma on 17/06/2016.
 */
class NnetVirtualFileListener : VirtualFileAdapter {
    val component : NnetEditorComponent
    constructor(component : NnetEditorComponent) : super(){
        this.component = component
    }

    override fun propertyChanged(event : VirtualFilePropertyEvent) {
        super.propertyChanged(event)
        if(VirtualFile.PROP_NAME.equals(event.propertyName)){
            component.updateValidProperty()
            if(Comparing.equal<VirtualFile>(event.file,component.file)
            && FileContentUtilCore.FORCE_RELOAD_REQUESTOR.equals(event.requestor)
            || !Comparing.equal<Any?>(event.oldValue,event.newValue)){
                component.updateHighlighters()
            }
        }
    }

    override fun contentsChanged(event : VirtualFileEvent) {
        super.contentsChanged(event)
        if(event.isFromSave){
            NnetEditorComponent.assertThread()
            val file : VirtualFile = event.file
            NnetEditorComponent.LOG.assertTrue(file.isValid)
            if(component.file!!.equals(file)){
                component.updateModifiedProperty()
            }
        }
    }
}