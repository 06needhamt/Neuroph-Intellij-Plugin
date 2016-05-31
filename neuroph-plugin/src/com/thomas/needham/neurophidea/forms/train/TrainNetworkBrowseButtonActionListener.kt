package com.thomas.needham.neurophidea.forms.train

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.consumers.ExportNetworkConsumer
import com.thomas.needham.neurophidea.forms.export.ExportNetworkForm
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_TRAIN_LOCATION_KEY;
import com.thomas.needham.neurophidea.actions.ShowTrainNetworkFormAction
import com.thomas.needham.neurophidea.consumers.NetworkToTrainConsumer
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by thoma on 31/05/2016.
 */
class TrainNetworkBrowseButtonActionListener : ActionListener {
    var formInstance : TrainNetworkForm? = null
    companion object Data{
        val defaultPath = ""
        val allowedFileTypes = arrayOf("conf")
        val fileDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        val consumer : NetworkToTrainConsumer? = NetworkToTrainConsumer()
        val properties = PropertiesComponent.getInstance()
        var chosenPath = ""
    }
    override fun actionPerformed(e : ActionEvent?) {
        properties?.setValue(NETWORK_TO_TRAIN_LOCATION_KEY,defaultPath)
        FileChooser.chooseFile(fileDescriptor,ShowTrainNetworkFormAction.project,null,consumer as Consumer<VirtualFile?>)
        chosenPath = properties.getValue(NETWORK_TO_TRAIN_LOCATION_KEY, defaultPath)
        formInstance?.txtNetwork?.text = chosenPath
    }
}