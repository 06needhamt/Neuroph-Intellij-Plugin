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
package com.thomas.needham.neurophidea.actions

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import com.intellij.util.Icons
import com.thomas.needham.neurophidea.actions.InitialisationAction
import com.thomas.needham.neurophidea.consumers.OpenNetworkConsumer
import com.thomas.needham.neurophidea.datastructures.NetworkConfiguration
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_OPEN_LOCATION_KEY
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream

/**
 * Created by thoma on 27/05/2016.
 */
class OpenExistingNetworkAction : AnAction() {

    companion object Data {
        var project : Project? = null
        var projectDirectory : String? = ""
        var isOpen : Boolean? = false
        val properties = PropertiesComponent.getInstance()
        val fileDesc : FileChooserDescriptor = FileChooserDescriptor(true, false, false, false, false, false)
        val consumer : OpenNetworkConsumer? = OpenNetworkConsumer()
        val createAction : ShowCreateNetworkFormAction? = ShowCreateNetworkFormAction()
        val getNetwork : (String) -> NetworkConfiguration? = { path -> //Use Some Kotlin Sorcery
            val network : NetworkConfiguration?
            try {
                val file = File(path)
                val fis = FileInputStream(file)
                val ois = ObjectInputStream(fis)
                network = ois.readObject() as NetworkConfiguration?
                network
            }
            catch (ioe: IOException){
                ioe.printStackTrace(System.err)
                Messages.showErrorDialog(OpenExistingNetworkAction.project,"Error Reading Network From file", "Error")
                null
            }
            catch (fnfe: FileNotFoundException){
                fnfe.printStackTrace(System.err)
                Messages.showErrorDialog(OpenExistingNetworkAction.project,"No Network Configuration Found at: " + properties.getValue(NETWORK_TO_OPEN_LOCATION_KEY,""), "Error")
                null
            }
        }

    }
    override fun actionPerformed(e : AnActionEvent) {
        project = InitialisationAction.project
        projectDirectory = InitialisationAction.projectDirectory
        isOpen = project?.isOpen
        FileChooser.chooseFile(fileDesc,project,null,consumer as Consumer<VirtualFile?>)
        val network : NetworkConfiguration? = getNetwork(properties.getValue(NETWORK_TO_OPEN_LOCATION_KEY,"")) ?: return
        val getLayersString : (Array<Int>) -> String = { layers -> //Use Some Kotlin Sorcery to convert Array<Int> to comma delimited string
            var result = ""
            for(i in IntRange(0,layers.size - 1)) {
                if(i == layers.size - 1)
                    result += layers[i].toString()
                else
                    result += (layers[i].toString() + ",")
            }
            result
        }
        createAction?.actionPerformed(e)
        createAction?.form?.isVisible = false
        createAction?.form?.txtNetworkName?.text = network?.networkName
        createAction?.form?.txtLayers?.text = getLayersString(network?.networkLayers!!)
        createAction?.form?.txtTrainingData?.text = network?.networkTrainingDataPath
        createAction?.form?.txtTestingData?.text = network?.networkTestingDataPath
        createAction?.form?.txtNetworkOutputPath?.text = network?.networkOutputPath
        createAction?.form?.isVisible = true

    }
}
