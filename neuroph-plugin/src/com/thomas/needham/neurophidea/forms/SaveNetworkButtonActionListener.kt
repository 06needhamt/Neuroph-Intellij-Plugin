package com.thomas.needham.neurophidea.forms

import com.intellij.openapi.ui.Messages
import com.intellij.util.Icons
import com.thomas.needham.neurophidea.Predicates.EqualToOrLessThan
import com.thomas.needham.neurophidea.actions.ShowCreateNetworkFormAction
import com.thomas.needham.neurophidea.datastructures.LearningRules
import com.thomas.needham.neurophidea.datastructures.NetworkConfiguration
import com.thomas.needham.neurophidea.datastructures.NetworkTypes
import com.thomas.needham.neurophidea.datastructures.TransferFunctions
import com.thomas.needham.neurophidea.exceptions.InvalidLayerSizeException
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
            Messages.showErrorDialog(ShowCreateNetworkFormAction.project,"Please Fill Out All Fields",
                    "Error")
            return
        }
        network = CreateNetworkConfiguration()
        if(network == null){
            Messages.showErrorDialog(ShowCreateNetworkFormAction.project,"Error Creating Network Configuration",
                    "Error")
            return
        }
        outputDirectory = network?.networkOutputPath!!
        configurationOutputPath = outputDirectory + "/" + network?.networkName + ".conf"
        networkFileOutputPath = outputDirectory + "/" + network?.networkName + ".nnet"
        codeFileOutputPath = outputDirectory + "/" + network?.networkName + ".java"
        if(!WriteNetworkToFile()){
            Messages.showErrorDialog(ShowCreateNetworkFormAction.project,"Error writing network configuration to file",
                    "Error")
        }
    }

    private fun WriteNetworkToFile() : Boolean {
        try{
            val file = File(configurationOutputPath)
            if(!file.exists())
                file.createNewFile()
            val fos = FileOutputStream(file,false)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(network)
            oos.flush()
            oos.close()
            Messages.showOkCancelDialog(ShowCreateNetworkFormAction.project,"Network Configuration Successfully written to "
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
        var error = false
        val getLayers : (String) -> Array<Int?> = { e ->
            val temp : List<String> = e.split(',')
            val result : Array<Int?> = arrayOfNulls<Int>(temp.size)
            try{
                for(i in IntRange(0,temp.size - 1 )){
                    result[i] = temp[i].toInt()
                    if(EqualToOrLessThan(result[i]!!,0)){
                        throw InvalidLayerSizeException("Layer Sizes must be greater than 0")
                    }
                }
                if(result.size < 2){
                    Messages.showErrorDialog(ShowCreateNetworkFormAction.project,"Networks must consist of at least 2 layers","Error")
                    error = true
                    arrayOf(0)
                }
                else if(type == NetworkTypes.Types.PERCEPTRON && result.size > 2){
                    Messages.showErrorDialog(ShowCreateNetworkFormAction.project, "Perceptron Network Type must consist of two layers", "Error")
                    error = true
                    arrayOf(0)
                }
                else if(type == NetworkTypes.Types.MULTI_LAYER_PERCEPTRON && result.size < 3){
                    Messages.showErrorDialog(ShowCreateNetworkFormAction.project,"Multi Layer Perceptron Network Type must consist of at least 3 layers","Error")
                    error = true
                    arrayOf(0)
                }
                //TODO add more validation rules
                result
            }
            catch (nfe: NumberFormatException){
                nfe.printStackTrace(System.err)
                Messages.showErrorDialog(ShowCreateNetworkFormAction.project, "Invalid Layers", "Error")
                error = true
                arrayOf(0)
            }
            catch(ilse: InvalidLayerSizeException){
                ilse.printStackTrace(System.err)
                Messages.showErrorDialog(ShowCreateNetworkFormAction.project,"Layer Sizes must be greater than 0","Error")
                error = true
                arrayOf(0)
            }
        } // More Kotlin Sorcery

        val layers : Array<Int> = getLayers(formInstance?.txtLayers?.text!!).filterNotNull().toTypedArray<Int>() // Even More Sorcery
        if(error)
            return null
        val learningRule : LearningRules.Rules = LearningRules.Rules.valueOf((formInstance?.cmbLearningRule?.selectedItem as String).replace(' ', '_').toUpperCase())
        val transferFunction : TransferFunctions.Functions = TransferFunctions.Functions.valueOf((formInstance?.cmbTransferFunction?.selectedItem as String).replace(' ', '_').toUpperCase())
        val trainingDataPath : String = formInstance?.txtTrainingData?.text!!
        val testingDataPath : String = formInstance?.txtTestingData?.text!!
        val networkOutputPath : String = formInstance?.txtNetworkOutputPath?.text!!
        network = NetworkConfiguration(name, type, layers, learningRule, transferFunction,
                trainingDataPath, testingDataPath,networkOutputPath)
        return network
    }

    private fun CheckInput() : Boolean {
        return !(formInstance?.txtLayers?.text.equals("") || formInstance?.txtNetworkName?.text.equals("") ||
                formInstance?.txtTrainingData?.text.equals("") || formInstance?.txtTestingData?.text.equals("") ||
                formInstance?.txtNetworkOutputPath?.text.equals(""))
    }

}
