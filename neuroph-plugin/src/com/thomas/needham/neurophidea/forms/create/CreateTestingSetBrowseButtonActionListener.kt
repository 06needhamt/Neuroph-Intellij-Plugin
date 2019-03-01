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
package com.thomas.needham.neurophidea.forms.create

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
 * Created by Thomas Needham on 28/05/2016.
 */
class CreateTestingSetBrowseButtonActionListener : ActionListener {
	var formInstance: CreateNetworkForm? = null
	
	companion object Data {
		val defaultPath = ""
		val allowedFileTypes = arrayOf("csv", "txt", "tset")
		val fileDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
		val consumer: TestingSetFileConsumer? = TestingSetFileConsumer()
		val properties = PropertiesComponent.getInstance()
		var chosenPath = ""
	}
	
	override fun actionPerformed(e: ActionEvent?) {
		CreateTestingSetBrowseButtonActionListener.properties?.setValue(TESTING_SET_LOCATION_KEY, CreateTestingSetBrowseButtonActionListener.defaultPath)
		FileChooser.chooseFile(CreateTestingSetBrowseButtonActionListener.fileDescriptor, ShowCreateNetworkFormAction.project, null, CreateTestingSetBrowseButtonActionListener.consumer as Consumer<VirtualFile?>)
		CreateTestingSetBrowseButtonActionListener.chosenPath = CreateTestingSetBrowseButtonActionListener.properties.getValue(TESTING_SET_LOCATION_KEY, CreateTestingSetBrowseButtonActionListener.defaultPath)
		formInstance?.txtTestingData?.text = CreateTestingSetBrowseButtonActionListener.chosenPath
	}
}