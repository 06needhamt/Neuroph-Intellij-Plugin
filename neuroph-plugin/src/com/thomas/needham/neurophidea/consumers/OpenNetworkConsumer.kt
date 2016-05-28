package com.thomas.needham.neurophidea.consumers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_OPEN_LOCATION_KEY
import com.thomas.needham.neurophidea.Constants.VERSION_KEY

/**
 * Created by thoma on 27/05/2016.
 */
class OpenNetworkConsumer  : Consumer<VirtualFile?> {
    constructor(){

    }
    companion object Data{
        @JvmStatic val properties = PropertiesComponent.getInstance()
        @JvmStatic var version = NetworkOutputFileConsumer.properties.getValue(VERSION_KEY)
        @JvmStatic var path : String? = ""
    }
    override fun consume(p0 : VirtualFile?) {
        path = p0?.path
        properties.setValue(NETWORK_TO_OPEN_LOCATION_KEY,path)
    }
}