package com.thomas.needham.neurophidea.consumers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.vfs.newvfs.impl.VirtualDirectoryImpl
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.NETWORK_RESULTS_LOCATION_KEY
import com.thomas.needham.neurophidea.Constants.VERSION_KEY

/**
 * Created by thoma on 04/06/2016.
 */
class NetworkResultsConsumer : Consumer<VirtualDirectoryImpl?> {
    constructor() {

    }

    companion object Data {
        @JvmStatic var properties = PropertiesComponent.getInstance()
        @JvmStatic var version = properties.getValue(VERSION_KEY)
        @JvmStatic var path : String? = ""
    }

    override fun consume(p0 : VirtualDirectoryImpl?) {
        path = p0?.path
        properties.setValue(NETWORK_RESULTS_LOCATION_KEY, path)
    }
}