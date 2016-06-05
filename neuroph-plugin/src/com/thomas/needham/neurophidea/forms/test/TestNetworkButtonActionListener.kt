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
import com.intellij.openapi.ui.Messages
import com.intellij.util.PlatformIcons
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_TEST_LOCATION_KEY
import com.thomas.needham.neurophidea.Constants.TEST_FORM_TESTING_SET_LOCATION_KEY
import com.thomas.needham.neurophidea.core.NetworkTester
import com.thomas.needham.neurophidea.core.NetworkTrainer
import com.thomas.needham.neurophidea.forms.train.TrainNetworkForm
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by thoma on 05/06/2016.
 */
class TestNetworkButtonActionListener : ActionListener {

    var formInstance : TestNetworkForm? = null
    var networkTester : NetworkTester? = null

    companion object Data {
        val properties = PropertiesComponent.getInstance()
    }

    override fun actionPerformed(e : ActionEvent?) {
        if (!formInstance?.txtTestingData?.text.equals("")) {
            val input = formInstance?.txtTestingData?.text
            val split = input?.split(",")
            val parsedOutput = DoubleArray(split?.size!!)
            for (i in 0..split?.size!!) {
                parsedOutput[i] = split!![i].toDouble()
            }
            networkTester = NetworkTester(properties.getValue(NETWORK_TO_TEST_LOCATION_KEY, ""), parsedOutput, formInstance?.txtOutputPath?.text!!)
        } else {
            networkTester = NetworkTester(properties.getValue(NETWORK_TO_TEST_LOCATION_KEY, ""), properties.getValue(TEST_FORM_TESTING_SET_LOCATION_KEY, ""), formInstance?.txtOutputPath?.text!!)
        }
        networkTester?.TestNetwork()
        Messages.showOkCancelDialog("Network Successfully Tested!", "Success", PlatformIcons.CHECK_ICON)
    }
}