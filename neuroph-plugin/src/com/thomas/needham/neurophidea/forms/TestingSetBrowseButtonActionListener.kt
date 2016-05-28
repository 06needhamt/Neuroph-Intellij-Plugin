package com.thomas.needham.neurophidea.forms

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.TESTING_SET_LOCATION_KEY
import com.thomas.needham.neurophidea.actions.ShowCreateNetworkFormAction
import com.thomas.needham.neurophidea.consumers.TestingSetFileConsumer
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by thoma on 28/05/2016.
 */
class TestingSetBrowseButtonActionListener : ActionListener {
    var formInstance : CreateNetworkForm? = null
    companion object Data{
        val defaultPath = ""
        val allowedFileTypes = arrayOf("csv", "txt", "tset")
        val fileDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        val consumer : TestingSetFileConsumer? = TestingSetFileConsumer()
        val properties = PropertiesComponent.getInstance()
        var chosenPath = ""
    }
    
    override fun actionPerformed(e : ActionEvent?) {
        TestingSetBrowseButtonActionListener.properties?.setValue(TESTING_SET_LOCATION_KEY, TestingSetBrowseButtonActionListener.defaultPath)
        FileChooser.chooseFile(TestingSetBrowseButtonActionListener.fileDescriptor, ShowCreateNetworkFormAction.project,null, TestingSetBrowseButtonActionListener.consumer as Consumer<VirtualFile?>)
        TestingSetBrowseButtonActionListener.chosenPath = TestingSetBrowseButtonActionListener.properties.getValue(TESTING_SET_LOCATION_KEY, TestingSetBrowseButtonActionListener.defaultPath)
        formInstance?.txtTestingData?.text = TestingSetBrowseButtonActionListener.chosenPath
    }
}