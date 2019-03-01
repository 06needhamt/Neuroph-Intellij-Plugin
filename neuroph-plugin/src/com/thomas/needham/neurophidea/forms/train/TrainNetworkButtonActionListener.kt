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
package com.thomas.needham.neurophidea.forms.train

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ui.Messages
import com.intellij.util.PlatformIcons
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_TRAIN_LOCATION_KEY
import com.thomas.needham.neurophidea.Constants.TRAIN_FORM_TRAINING_SET_LOCATION_KEY
import com.thomas.needham.neurophidea.core.NetworkTrainer
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

/**
 * Created by Thomas Needham on 31/05/2016.
 */
class TrainNetworkButtonActionListener : ActionListener {
	var formInstance: TrainNetworkForm? = null
	var networkTrainer: NetworkTrainer? = null
	
	companion object Data {
		val properties = PropertiesComponent.getInstance()
	}
	
	override fun actionPerformed(e: ActionEvent?) {
		if (!formInstance?.txtInputData?.text.equals("")) {
			val input = formInstance?.txtInputData?.text
			val split = input?.split(",")
			val parsedOutput = DoubleArray(split?.size!!)
			for (i in 0..split?.size!!) {
				parsedOutput[i] = split!![i].toDouble()
			}
			networkTrainer = NetworkTrainer(properties.getValue(NETWORK_TO_TRAIN_LOCATION_KEY, ""), parsedOutput)
		} else {
			networkTrainer = NetworkTrainer(properties.getValue(NETWORK_TO_TRAIN_LOCATION_KEY, ""), properties.getValue(TRAIN_FORM_TRAINING_SET_LOCATION_KEY, ""))
		}
		networkTrainer?.TrainNetwork()
		Messages.showOkCancelDialog("Network Successfully Trained!", "Success", PlatformIcons.CHECK_ICON)
	}
}