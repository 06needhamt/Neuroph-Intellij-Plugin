package com.thomas.needham.neurophidea.consumers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.TRAIN_FORM_TRAINING_SET_LOCATION_KEY
import com.thomas.needham.neurophidea.Constants.VERSION_KEY
import com.thomas.needham.neurophidea.actions.ShowCreateNetworkFormAction
import com.thomas.needham.neurophidea.forms.create.TrainingSetBrowseButtonActionListener

/**
 * Created by thoma on 31/05/2016.
 */
class TrainNetworkSetFileConsumer : Consumer<VirtualFile?> {

    constructor(){

    }
    companion object Data{
        @JvmStatic var properties = PropertiesComponent.getInstance()
        @JvmStatic var version = properties.getValue(VERSION_KEY)
        @JvmStatic var path : String? = ""
    }

    override fun consume(p0 : VirtualFile?) {
        for(ext : String in TrainingSetBrowseButtonActionListener.allowedFileTypes){
            if(p0?.extension?.equals(ext)!!){
                path = p0?.path
                properties.setValue(TRAIN_FORM_TRAINING_SET_LOCATION_KEY, path)
                return
            }
        }
        Messages.showErrorDialog(ShowCreateNetworkFormAction.project,"Invalid training data selected","Error")
    }
}