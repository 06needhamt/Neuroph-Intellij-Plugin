package com.thomas.needham.neurophidea.forms.train

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.Messages
import com.intellij.util.Icons
import com.thomas.needham.neurophidea.consumers.NetworkToTrainConsumer
import com.thomas.needham.neurophidea.core.NetworkTrainer
import com.thomas.needham.neurophidea.datastructures.NetworkConfiguration
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_TRAIN_LOCATION_KEY
import com.thomas.needham.neurophidea.Constants.TRAIN_FORM_TRAINING_SET_LOCATION_KEY
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by thoma on 31/05/2016.
 */
class TrainNetworkButtonActionListener : ActionListener{
    var formInstance : TrainNetworkForm? = null
    var networkTrainer : NetworkTrainer? = null

    companion object Data{
        val properties = PropertiesComponent.getInstance()
    }

    override fun actionPerformed(e : ActionEvent?) {
        if(!formInstance?.txtInputData?.text.equals("")){
            var input = formInstance?.txtInputData?.text
            var split = input?.split(",")
            var parsedOutput = DoubleArray(split?.size!!)
            for(i in 0..split?.size!!){
                parsedOutput[i] = split!![i].toDouble()
            }
            networkTrainer = NetworkTrainer(properties.getValue(NETWORK_TO_TRAIN_LOCATION_KEY,""), parsedOutput)
        }
        else{
            networkTrainer = NetworkTrainer(properties.getValue(NETWORK_TO_TRAIN_LOCATION_KEY,""),properties.getValue(TRAIN_FORM_TRAINING_SET_LOCATION_KEY,""))
        }
        networkTrainer?.TrainNetwork()
        Messages.showOkCancelDialog("Network Successfully Trained!","Success", Icons.CHECK_ICON)
    }
}