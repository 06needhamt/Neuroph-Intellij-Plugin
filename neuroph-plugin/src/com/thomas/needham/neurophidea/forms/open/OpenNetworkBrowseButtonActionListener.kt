package com.thomas.needham.neurophidea.forms.open

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_OPEN_LOCATION_KEY
import com.thomas.needham.neurophidea.actions.ShowCreateNetworkFormAction
import com.thomas.needham.neurophidea.consumers.OpenNetworkConsumer
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by thoma on 13/06/2016.
 */
class OpenNetworkBrowseButtonActionListener : ActionListener {
	var formInstance: OpenNetworkForm? = null
	
	companion object Data {
		val defaultPath = ""
		val allowedFileTypes = arrayOf("nnet")
		val fileDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
		val consumer: OpenNetworkConsumer? = OpenNetworkConsumer()
		val properties = PropertiesComponent.getInstance()
		var chosenPath = ""
	}
	
	override fun actionPerformed(e: ActionEvent?) {
		properties?.setValue(NETWORK_TO_OPEN_LOCATION_KEY, defaultPath)
		FileChooser.chooseFile(fileDescriptor, ShowCreateNetworkFormAction.project, null, consumer as Consumer<VirtualFile?>)
		chosenPath = properties.getValue(NETWORK_TO_OPEN_LOCATION_KEY, defaultPath)
		formInstance?.txtNetworkToOpen?.text = chosenPath
	}
}