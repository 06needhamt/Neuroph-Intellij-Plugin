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
import com.intellij.openapi.ui.Messages
import com.intellij.util.PlatformIcons
import com.thomas.needham.neurophidea.actions.ShowNetworkOutputFormAction
import com.thomas.needham.neurophidea.core.NetworkTester
import com.thomas.needham.neurophidea.forms.test.TestNetworkForm
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.util.*

/**
 * Created by thoma on 09/06/2016.
 */
class SaveOutputButtonActionListener  : ActionListener {
    var formInstance : NetworkOutputForm? = null
    var file : File? = null

    companion object Data {
        val properties = PropertiesComponent.getInstance()
        val defaultPath = ""
        var path = ""
    }
    override fun actionPerformed(e : ActionEvent?) {
        path = formInstance?.txtSaveLocation?.text!!
        file = File((path + "/" + "Output ${Date().toString().replace(':', '-')}.output").replace(' ', '_'))
        val fw = FileWriter(file,false)
        val bw = BufferedWriter(fw)
        for(i in 0..formInstance?.lstActual?.model?.size!! - 1){
            bw.write(formInstance?.lstActual?.model?.getElementAt(i).toString() + ",")
            bw.write(formInstance?.lstExpected?.model?.getElementAt(i).toString() + ",")
            bw.write(formInstance?.lstDifference?.model?.getElementAt(i).toString() + "\n")
            bw.flush()
        }
        bw.flush()
        bw.close()
        Messages.showOkCancelDialog(ShowNetworkOutputFormAction.project,"Output Written Successfully to file: ${file?.path}","Success",PlatformIcons.CHECK_ICON)
    }
}