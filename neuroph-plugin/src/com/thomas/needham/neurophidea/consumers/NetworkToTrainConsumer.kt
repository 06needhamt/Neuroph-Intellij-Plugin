package com.thomas.needham.neurophidea.consumers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_TRAIN_LOCATION_KEY
import com.thomas.needham.neurophidea.Constants.VERSION_KEY
import com.thomas.needham.neurophidea.forms.train.TrainNetworkBrowseButtonActionListener

/**
 * Created by thoma on 31/05/2016.
 */
class NetworkToTrainConsumer : Consumer<VirtualFile?> {
    constructor(){

    }
    companion object Data{
        @JvmStatic var properties = PropertiesComponent.getInstance()
        @JvmStatic var version = properties.getValue(VERSION_KEY)
        @JvmStatic var path : String? = ""
    }
    override fun consume(p0 : VirtualFile?) {
        for(type : String in TrainNetworkBrowseButtonActionListener.allowedFileTypes){
            if(p0?.extension?.equals(type)!!){
                path = p0?.path
                properties.setValue(NETWORK_TO_TRAIN_LOCATION_KEY,path)
                return
            }
        }
        Messages.showErrorDialog("Invalid Network File Type: ${p0?.extension}","Error")
    }

}