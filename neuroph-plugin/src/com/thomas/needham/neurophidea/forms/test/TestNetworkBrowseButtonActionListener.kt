package com.thomas.needham.neurophidea.forms.test

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_TEST_LOCATION_KEY
import com.thomas.needham.neurophidea.actions.ShowTestNetworkFormAction
import com.thomas.needham.neurophidea.actions.ShowTrainNetworkFormAction
import com.thomas.needham.neurophidea.consumers.NetworkToTestConsumer
import com.thomas.needham.neurophidea.consumers.NetworkToTrainConsumer
import com.thomas.needham.neurophidea.forms.test.TestNetworkForm
import com.thomas.needham.neurophidea.forms.train.TrainNetworkForm
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by thoma on 04/06/2016.
 */
class TestNetworkBrowseButtonActionListener : ActionListener {

    var formInstance : TestNetworkForm? = null
    companion object Data{
        val defaultPath = ""
        val allowedFileTypes = arrayOf("nnet")
        val fileDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        val consumer : NetworkToTestConsumer? = NetworkToTestConsumer()
        val properties = PropertiesComponent.getInstance()
        var chosenPath = ""
    }
    override fun actionPerformed(e : ActionEvent?) {
        properties?.setValue(NETWORK_TO_TEST_LOCATION_KEY, defaultPath)
        FileChooser.chooseFile(fileDescriptor, ShowTestNetworkFormAction.project,null, consumer as Consumer<VirtualFile?>)
        chosenPath = properties.getValue(NETWORK_TO_TEST_LOCATION_KEY, defaultPath)
        formInstance?.txtNetworkToTest?.text = chosenPath
    }
}