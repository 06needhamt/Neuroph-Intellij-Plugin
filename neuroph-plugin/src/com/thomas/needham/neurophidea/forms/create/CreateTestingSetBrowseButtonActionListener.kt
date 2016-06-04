package com.thomas.needham.neurophidea.forms.create

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.TESTING_SET_LOCATION_KEY
import com.thomas.needham.neurophidea.actions.ShowCreateNetworkFormAction
import com.thomas.needham.neurophidea.consumers.TestingSetFileConsumer
import com.thomas.needham.neurophidea.forms.create.CreateNetworkForm
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by thoma on 28/05/2016.
 */
class CreateTestingSetBrowseButtonActionListener : ActionListener {
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
        CreateTestingSetBrowseButtonActionListener.properties?.setValue(TESTING_SET_LOCATION_KEY, CreateTestingSetBrowseButtonActionListener.defaultPath)
        FileChooser.chooseFile(CreateTestingSetBrowseButtonActionListener.fileDescriptor, ShowCreateNetworkFormAction.project,null, CreateTestingSetBrowseButtonActionListener.consumer as Consumer<VirtualFile?>)
        CreateTestingSetBrowseButtonActionListener.chosenPath = CreateTestingSetBrowseButtonActionListener.properties.getValue(TESTING_SET_LOCATION_KEY, CreateTestingSetBrowseButtonActionListener.defaultPath)
        formInstance?.txtTestingData?.text = CreateTestingSetBrowseButtonActionListener.chosenPath
    }
}