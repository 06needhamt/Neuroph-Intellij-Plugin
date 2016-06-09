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
package com.thomas.needham.neurophidea.forms.output

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.thomas.needham.neurophidea.Constants.NETWORK_RESULTS_OUTPUT_LOCATION_KEY
import com.thomas.needham.neurophidea.actions.ShowNetworkOutputFormAction
import com.thomas.needham.neurophidea.actions.ShowTestNetworkFormAction
import com.thomas.needham.neurophidea.consumers.ExportSourceConsumer
import com.thomas.needham.neurophidea.consumers.NetworkResultsOutputConsumer
import com.thomas.needham.neurophidea.forms.test.NetworkResultsBrowseActionListener
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by thoma on 09/06/2016.
 */
class ResultsOutputBrowseButtonActionListener : ActionListener {
    var formInstance : NetworkOutputForm? = null
    companion object Data{
        val defaultPath = ""
        val allowedFileTypes = arrayOf("output")
        val fileDescriptor = FileChooserDescriptor(false, true, false, false, false, false)
        val consumer : NetworkResultsOutputConsumer? = NetworkResultsOutputConsumer()
        val properties = PropertiesComponent.getInstance()
        var chosenPath = ""
    }
    override fun actionPerformed(e : ActionEvent?) {
        properties?.setValue(NETWORK_RESULTS_OUTPUT_LOCATION_KEY, defaultPath)
        FileChooser.chooseFile(fileDescriptor, ShowNetworkOutputFormAction.project,null, consumer as Consumer<VirtualFile?>)
        chosenPath = properties.getValue(NETWORK_RESULTS_OUTPUT_LOCATION_KEY, defaultPath)
        formInstance?.txtSaveLocation?.text = chosenPath
    }
}