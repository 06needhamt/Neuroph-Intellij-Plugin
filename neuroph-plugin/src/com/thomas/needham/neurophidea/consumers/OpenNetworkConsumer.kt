package com.thomas.needham.neurophidea.consumers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.actions.OpenExistingNetworkAction

/**
 * Created by thoma on 27/05/2016.
 */
class OpenNetworkConsumer  : Consumer<VirtualFile?> {
    constructor(){

    }
    companion object Data{
        @JvmStatic val properties = PropertiesComponent.getInstance(OpenExistingNetworkAction.project)
    }
    override fun consume(p0 : VirtualFile?) {
        throw UnsupportedOperationException()
    }
}