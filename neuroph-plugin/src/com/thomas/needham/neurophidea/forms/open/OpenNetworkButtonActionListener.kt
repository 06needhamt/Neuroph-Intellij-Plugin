package com.thomas.needham.neurophidea.forms.open

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ui.Messages
import com.thomas.needham.neurophidea.actions.InitialisationAction
import org.neuroph.core.NeuralNetwork
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowEvent
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream

/**
 * Created by thoma on 13/06/2016.
 */
class OpenNetworkButtonActionListener  : ActionListener {
    var formInstance : OpenNetworkForm? = null
    companion object Data{
        val properties = PropertiesComponent.getInstance()
        val openNetwork : (String) -> NeuralNetwork? = { path ->
            try{
                val file = File(path)
                val fis = FileInputStream(file)
                val ios = ObjectInputStream(fis)
                ios.readObject() as NeuralNetwork?
            }
            catch(ioe: IOException){
                ioe.printStackTrace(System.err)
                Messages.showErrorDialog(InitialisationAction.project, "Error Loading Network From File","Error")
                null
            }
            catch(fnfe: FileNotFoundException){
                fnfe.printStackTrace(System.err)
                Messages.showErrorDialog(InitialisationAction.project, "No Network Found In File","Error")
                null

            }
        }
    }
    override fun actionPerformed(e : ActionEvent?) {
        formInstance?.network = openNetwork(formInstance?.txtNetworkToOpen?.text!!)
        if(formInstance?.shouldClose!!){
            formInstance?.dispatchEvent(WindowEvent(formInstance,WindowEvent.WINDOW_CLOSING))
            formInstance?.dispose()
        }
    }
}