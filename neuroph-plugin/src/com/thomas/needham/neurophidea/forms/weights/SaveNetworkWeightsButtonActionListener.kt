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
package com.thomas.needham.neurophidea.forms.weights

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ui.Messages
import com.thomas.needham.neurophidea.actions.InitialisationAction
import org.neuroph.core.NeuralNetwork
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.*

/**
 * Created by thoma on 14/06/2016.
 */
class SaveNetworkWeightsButtonActionListener : ActionListener {
	var formInstance: NetworkWeightsForm? = null
	var network: NeuralNetwork? = null
	
	companion object Data {
		val properties = PropertiesComponent.getInstance()
		var path = ""
		val getNetwork: (String) -> NeuralNetwork? = { path ->
			try {
				val file = File(path)
				val fis = FileInputStream(file)
				val ois = ObjectInputStream(fis)
				ois.readObject() as NeuralNetwork?
			} catch (ioe: IOException) {
				ioe.printStackTrace(System.err)
				Messages.showErrorDialog(InitialisationAction.project, "Error Loading Network From File: ${path}", "Error")
				null
			} catch (fnfe: FileNotFoundException) {
				fnfe.printStackTrace(System.err)
				Messages.showErrorDialog(InitialisationAction.project, "No Network found in: ${path}", "Error")
				null
			}
		}
		
	}
	
	override fun actionPerformed(e: ActionEvent?) {
		path = formInstance?.path!!
		network = formInstance?.network
		if (network == null)
			network = getNetwork(path)
		val setWeights: () -> Boolean = {
			try {
				for (i in 0..formInstance?.weights?.size!! - 1) {
					for (j in 0..formInstance?.weights!![i].size - 1) {
						formInstance?.weights!![i][j] = formInstance?.fields!![i][j].text.toDouble()
					}
				}
				for (i in 0..formInstance?.network?.layers?.size!! - 1) {
					for (j in 0..formInstance?.network?.layers!![i]?.neurons?.size!! - 1) {
						formInstance?.network?.layers!![i]?.neurons!![j]?.weightsVector!![0].setValue(formInstance?.weights!![i][j])
					}
				}
				network?.save(path)
				Messages.showErrorDialog(InitialisationAction.project, "Network Weights Successfully Updated", "Success")
				true
			} catch (nfe: NumberFormatException) {
				nfe.printStackTrace(System.err)
				Messages.showErrorDialog(InitialisationAction.project, "Invalid Network Weight Specified", "Error")
				false
			}
		}
		if (!setWeights()) {
			Messages.showErrorDialog(InitialisationAction.project, "Error Updating Network Weights", "Error")
		}
	}
}