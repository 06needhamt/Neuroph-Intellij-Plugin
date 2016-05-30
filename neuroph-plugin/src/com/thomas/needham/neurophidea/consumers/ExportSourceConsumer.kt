package com.thomas.needham.neurophidea.consumers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.vfs.newvfs.impl.VirtualDirectoryImpl
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.SOURCE_TO_EXPORT_LOCATION_KEY
import com.thomas.needham.neurophidea.Constants.VERSION_KEY

/**
 * Created by thoma on 29/05/2016.
 */
class ExportSourceConsumer : Consumer<VirtualDirectoryImpl?> {
    constructor(){

    }
    companion object Data{
        @JvmStatic var properties = PropertiesComponent.getInstance()
        @JvmStatic var version = properties.getValue(VERSION_KEY)
        @JvmStatic var path : String? = ""
    }
    override fun consume(p0 : VirtualDirectoryImpl?) {
        ExportNetworkConsumer.path = p0?.path
        ExportNetworkConsumer.properties.setValue(SOURCE_TO_EXPORT_LOCATION_KEY, ExportNetworkConsumer.path)
    }
}