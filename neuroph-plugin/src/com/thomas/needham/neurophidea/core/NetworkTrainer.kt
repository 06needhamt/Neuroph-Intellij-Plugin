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
package com.thomas.needham.neurophidea.core

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ui.Messages
import com.thomas.needham.neurophidea.Constants
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_TRAIN_LOCATION_KEY
import com.thomas.needham.neurophidea.actions.OpenExistingNetworkAction
import com.thomas.needham.neurophidea.datastructures.NetworkConfiguration
import com.thomas.needham.neurophidea.Constants.COMMA_DELIMITED
import com.thomas.needham.neurophidea.actions.ShowTrainNetworkFormAction
import com.thomas.needham.neurophidea.datastructures.LearningRules
import com.thomas.needham.neurophidea.datastructures.NetworkTypes
import com.thomas.needham.neurophidea.datastructures.TransferFunctions
import com.thomas.needham.neurophidea.exceptions.UnknownLearningRuleException
import com.thomas.needham.neurophidea.exceptions.UnknownNetworkTypeException
import com.thomas.needham.neurophidea.exceptions.UnknownTransferFunctionException
import org.neuroph.core.NeuralNetwork
import org.neuroph.core.learning.SupervisedTrainingElement
import org.neuroph.core.learning.TrainingSet
import org.neuroph.nnet.MultiLayerPerceptron
import org.neuroph.nnet.Perceptron
import org.neuroph.nnet.learning.BackPropagation
import org.neuroph.nnet.learning.DynamicBackPropagation
import org.neuroph.util.TransferFunctionType
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream

/**
 * Created by thoma on 30/05/2016.
 */
class NetworkTrainer {
    var network : NeuralNetwork? = null
    var trainingSet : TrainingSet<SupervisedTrainingElement?>? = null
    var trainingData : DoubleArray? = null
    var properties = PropertiesComponent.getInstance()
    var inputSize : Int = 0
    var outputSize : Int = 0

    companion object Data{
        var networkPath = ""
        var trainingSetPath = ""
        var networkConfiguration : NetworkConfiguration? = null
        var outputPath = ""
    }

    constructor(path: String, trainingSet: String){
        networkPath = path
        trainingSetPath = trainingSet
        networkConfiguration = LoadNetworkConfiguration()
        this.trainingSet = LoadTrainingSet()

    }
    constructor(path : String, trainingData : DoubleArray){
        networkPath = path
        this.trainingData = trainingData
        networkConfiguration = LoadNetworkConfiguration()
        this.trainingSet = CreateTrainingSet()
    }

    private fun LoadNetworkConfiguration() : NetworkConfiguration?{
        val network : NetworkConfiguration?
        try {
            val file = File(networkPath)
            val fis = FileInputStream(file)
            val ois = ObjectInputStream(fis)
            network = ois.readObject() as NetworkConfiguration?
            inputSize = network?.networkLayers?.first()!!
            outputSize = network?.networkLayers?.last()!!
            return network
        }
        catch (ioe: IOException){
            ioe.printStackTrace(System.err)
            Messages.showErrorDialog(OpenExistingNetworkAction.project,"Error Reading Network From file", "Error")
            return null
        }
        catch (fnfe: FileNotFoundException){
            fnfe.printStackTrace(System.err)
            Messages.showErrorDialog(OpenExistingNetworkAction.project, "No Network Configuration Found at: ${ShowTrainNetworkFormAction.properties.getValue(NETWORK_TO_TRAIN_LOCATION_KEY, "")}", "Error")
            return null
        }
    }

    private fun CreateTrainingSet() : TrainingSet<SupervisedTrainingElement?>?{
        if(trainingData == null)
            return null
        try {
            var set = TrainingSet<SupervisedTrainingElement?>(networkConfiguration?.networkLayers?.first()!!, networkConfiguration?.networkLayers?.last()!!)
            for (i in 0..trainingData?.size!! step inputSize + outputSize) {
                val inputSize = networkConfiguration?.networkLayers?.first()!!
                val outputSize = networkConfiguration?.networkLayers?.last()!!
                set.addElement(SupervisedTrainingElement(trainingData!!.copyOfRange(i, (i + inputSize)),
                        trainingData!!.copyOfRange((i + inputSize), (i + inputSize + outputSize))))
            }
            return set
        }
        catch (ioobe: IndexOutOfBoundsException){
            ioobe.printStackTrace(System.err)
            Messages.showErrorDialog(ShowTrainNetworkFormAction.project, "Training data does not contain the correct amount of inputs or outputs","Error")
            return null
        }
    }
    private fun LoadTrainingSet() : TrainingSet<SupervisedTrainingElement?>?{
        return TrainingSet.createFromFile(trainingSetPath,inputSize,outputSize,COMMA_DELIMITED) as TrainingSet<SupervisedTrainingElement?>?
    }

    fun TrainNetwork() : NeuralNetwork?{
        var function : TransferFunctionType? = null
        when (networkConfiguration?.networkTransferFunction){
            TransferFunctions.Functions.SIGMOID -> function = TransferFunctionType.SIGMOID
            TransferFunctions.Functions.GAUSSIAN -> function = TransferFunctionType.GAUSSIAN
            else -> UnknownTransferFunctionException("Unknown Transfer Function: ${TransferFunctions.GetClassName(networkConfiguration?.networkTransferFunction!!)}")
            //TODO add more transfer functions
        }
        when (networkConfiguration?.networkType){
            NetworkTypes.Types.PERCEPTRON -> network = Perceptron(inputSize,outputSize,function)
            NetworkTypes.Types.MULTI_LAYER_PERCEPTRON -> network = MultiLayerPerceptron(networkConfiguration?.networkLayers?.toList(),function)
            else -> UnknownNetworkTypeException("Unknown Network Type: ${NetworkTypes.GetClassName(networkConfiguration?.networkType!!)}")
            //TODO add more network types
        }
        when (networkConfiguration?.networkLearningRule){
            LearningRules.Rules.BACK_PROPAGATION -> network?.learningRule = BackPropagation()
            LearningRules.Rules.DYNAMIC_BACK_PROPAGATION -> network?.learningRule = DynamicBackPropagation()
            else -> UnknownLearningRuleException("Unknown Learning Rule: ${LearningRules.GetClassName(networkConfiguration?.networkLearningRule!!)}")
            //TODO add more learning rules
        }
        if(trainingSet == null) {
            Messages.showErrorDialog("Invalid Training Set","Error")
            return null
        }
        val outfile = File(networkPath + ".nnet")
        properties.setValue(NETWORK_TO_TRAIN_LOCATION_KEY,networkPath)
        if(!outfile.exists()){
            outfile.createNewFile()
        }
        network?.learn(trainingSet)
        network?.save(networkPath + ".nnet")
        return network
    }
}