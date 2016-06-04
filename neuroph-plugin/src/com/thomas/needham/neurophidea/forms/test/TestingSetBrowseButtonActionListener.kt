package com.thomas.needham.neurophidea.forms.test

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.TEST_FORM_TESTING_SET_LOCATION_KEY
import com.thomas.needham.neurophidea.actions.ShowTestNetworkFormAction
import com.thomas.needham.neurophidea.actions.ShowTrainNetworkFormAction
import com.thomas.needham.neurophidea.consumers.TestNetworkSetFileConsumer
import com.thomas.needham.neurophidea.consumers.TrainNetworkSetFileConsumer
import com.thomas.needham.neurophidea.forms.train.TrainNetworkForm
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by thoma on 04/06/2016.
 */
class TestingSetBrowseButtonActionListener : ActionListener {

    var formInstance : TestNetworkForm? = null
    companion object Data{
        val defaultPath = ""
        val allowedFileTypes = arrayOf("csv","tset")
        val fileDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        val consumer : TestNetworkSetFileConsumer? = TestNetworkSetFileConsumer()
        val properties = PropertiesComponent.getInstance()
        var chosenPath = ""
    }
    override fun actionPerformed(e : ActionEvent?) {
        properties?.setValue(TEST_FORM_TESTING_SET_LOCATION_KEY, defaultPath)
        FileChooser.chooseFile(fileDescriptor, ShowTestNetworkFormAction.project,null, consumer as Consumer<VirtualFile?>)
        chosenPath = properties.getValue(TEST_FORM_TESTING_SET_LOCATION_KEY, defaultPath)
        formInstance?.txtTestingSet?.text = chosenPath
    }
}