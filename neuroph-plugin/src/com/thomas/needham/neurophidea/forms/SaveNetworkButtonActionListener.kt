package com.thomas.needham.neurophidea.forms

import com.intellij.openapi.ui.Messages
import com.intellij.util.Icons
import com.thomas.needham.neurophidea.CreateNetworkAction
import com.thomas.needham.neurophidea.LearningRules
import com.thomas.needham.neurophidea.NetworkConfiguration
import com.thomas.needham.neurophidea.NetworkTypes
import com.thomas.needham.neurophidea.TransferFunctions
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectOutputStream

/**
 * Created by thoma on 27/05/2016.
 */
class SaveNetworkButtonActionListener : ActionListener {
    var formInstance : CreateNetworkForm? = null
    var network : NetworkConfiguration? = null
    companion object Data{
        var outputDirectory = ""
        var configurationOutputPath = ""
        var networkFileOutputPath = ""
        var codeFileOutputPath = ""
    }
    override fun actionPerformed(e : ActionEvent?) {
        if(!CheckInput()){
            Messages.showOkCancelDialog(CreateNetworkAction.project,"Please Fill Out All Fields",
                    "Error", Icons.ERROR_INTRODUCTION_ICON)
            return
        }
        network = CreateNetworkConfiguration()
        if(network == null){
            Messages.showOkCancelDialog(CreateNetworkAction.project,"Error Creating Network Configuration",
                    "Error", Icons.ERROR_INTRODUCTION_ICON)
            return
        }
        outputDirectory = network?.networkOutputPath!!
        configurationOutputPath = outputDirectory + "\\" + network?.networkName + ".conf"
        networkFileOutputPath = outputDirectory + "\\" + network?.networkName + ".nnet"
        codeFileOutputPath = outputDirectory + "\\" + network?.networkName + ".java"
        if(!WriteNetworkToFile()){
            Messages.showOkCancelDialog(CreateNetworkAction.project,"Error writing network configuration to file",
                    "Error", Icons.ERROR_INTRODUCTION_ICON)
        }
    }

    private fun WriteNetworkToFile() : Boolean {
        try{
            val file = File(configurationOutputPath)
            val fos = FileOutputStream(file,false)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(network)
            oos.flush()
            oos.close()
            Messages.showOkCancelDialog(CreateNetworkAction.project,"Network Configuration Successfully written to "
                    + configurationOutputPath, "Success",Icons.COPY_ICON)
            return true
        }
        catch(ex: IOException){
            ex.printStackTrace(System.err)
            return false
        }
    }

    private fun CreateNetworkConfiguration() : NetworkConfiguration? {
        val name : String = formInstance?.txtNetworkName?.text!!
        val type : NetworkTypes.Types = NetworkTypes.Types.valueOf((formInstance?.cmbNetworkType?.selectedItem as String).replace(' ', '_').toUpperCase())
        val getLayers : (String) -> Array<Int?> = { e ->
            val temp : List<String> = e.split(',')
            val result : Array<Int?> = arrayOfNulls<Int>(temp.size)
            try{
                for(i in IntRange(0,temp.size - 1 )){
                    result[i] = temp[i].toInt()
                }
                result
            }
            catch (ex: NumberFormatException){
                println(ex.printStackTrace(System.err))
                Messages.showOkCancelDialog("Invalid Layers", "Error", Icons.ERROR_INTRODUCTION_ICON)
                arrayOf(0)
            }
        } // More Kotlin Sorcery
        val layers : Array<Int> = getLayers(formInstance?.txtLayers?.text!!).filterNotNull().toTypedArray<Int>() // Even More Sorcery
        val learningRule : LearningRules.Rules = LearningRules.Rules.valueOf((formInstance?.cmbLearningRule?.selectedItem as String).replace(' ', '_').toUpperCase())
        val transferFunction : TransferFunctions.Functions = TransferFunctions.Functions.valueOf((formInstance?.cmbTransferFunction?.selectedItem as String).replace(' ', '_').toUpperCase())
        val trainingDataPath : String = formInstance?.txtTrainingData?.text!!
        val networkOutputPath : String = formInstance?.txtNetworkOutputPath?.text!!
        network = NetworkConfiguration(name,type,layers,learningRule,transferFunction,trainingDataPath,networkOutputPath)
        return network
    }

    private fun CheckInput() : Boolean {
        return !(formInstance?.txtLayers?.text.equals("") || formInstance?.txtNetworkName?.text.equals("") ||
                formInstance?.txtTrainingData?.text.equals("") || formInstance?.txtNetworkOutputPath?.text.equals(""))
    }

}
