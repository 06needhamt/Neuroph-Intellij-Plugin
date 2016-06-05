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