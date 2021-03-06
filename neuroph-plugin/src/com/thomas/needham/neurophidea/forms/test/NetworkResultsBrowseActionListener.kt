/*
The MIT License (MIT)

Copyright (c) 2016 Tom Needham

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.thomas.needham.neurophidea.forms.test

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.NETWORK_RESULTS_LOCATION_KEY
import com.thomas.needham.neurophidea.actions.ShowTestNetworkFormAction
import com.thomas.needham.neurophidea.consumers.NetworkResultsConsumer
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by Thomas Needham on 04/06/2016.
 */
class NetworkResultsBrowseActionListener : ActionListener {
	var formInstance: TestNetworkForm? = null
	
	companion object Data {
		val defaultPath = ""
		val fileDescriptor = FileChooserDescriptor(false, true, false, false, false, false)
		val consumer: NetworkResultsConsumer? = NetworkResultsConsumer()
		val properties = PropertiesComponent.getInstance()
		var chosenPath = ""
	}
	
	override fun actionPerformed(e: ActionEvent?) {
		properties?.setValue(NETWORK_RESULTS_LOCATION_KEY, defaultPath)
		FileChooser.chooseFile(fileDescriptor, ShowTestNetworkFormAction.project, null, consumer as Consumer<VirtualFile?>)
		chosenPath = properties.getValue(NETWORK_RESULTS_LOCATION_KEY, defaultPath)
		formInstance?.txtOutputPath?.text = chosenPath
	}
}