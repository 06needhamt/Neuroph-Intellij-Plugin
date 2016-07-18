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
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_TRAIN_LOCATION_KEY
import com.thomas.needham.neurophidea.actions.OpenExistingNetworkConfigurationAction
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
import org.neuroph.nnet.Adaline
import org.neuroph.nnet.BAM
import org.neuroph.nnet.CompetitiveNetwork
import org.neuroph.nnet.Hopfield
import org.neuroph.nnet.Instar
import org.neuroph.nnet.Kohonen
import org.neuroph.nnet.MaxNet
import org.neuroph.nnet.MultiLayerPerceptron
import org.neuroph.nnet.NeuroFuzzyPerceptron
import org.neuroph.nnet.Neuroph
import org.neuroph.nnet.Outstar
import org.neuroph.nnet.Perceptron
import org.neuroph.nnet.RbfNetwork
import org.neuroph.nnet.SupervisedHebbianNetwork
import org.neuroph.nnet.UnsupervisedHebbianNetwork
import org.neuroph.nnet.learning.AntiHebbianLearning
import org.neuroph.nnet.learning.BackPropagation
import org.neuroph.nnet.learning.BinaryDeltaRule
import org.neuroph.nnet.learning.BinaryHebbianLearning
import org.neuroph.nnet.learning.CompetitiveLearning
import org.neuroph.nnet.learning.DynamicBackPropagation
import org.neuroph.nnet.learning.GeneralizedHebbianLearning
import org.neuroph.nnet.learning.HopfieldLearning
import org.neuroph.nnet.learning.InstarLearning
import org.neuroph.nnet.learning.KohonenLearning
import org.neuroph.nnet.learning.LMS
import org.neuroph.nnet.learning.MomentumBackpropagation
import org.neuroph.nnet.learning.OjaLearning
import org.neuroph.nnet.learning.OutstarLearning
import org.neuroph.nnet.learning.PerceptronLearning
import org.neuroph.nnet.learning.ResilientPropagation
import org.neuroph.nnet.learning.SigmoidDeltaRule
import org.neuroph.nnet.learning.SimulatedAnnealingLearning
import org.neuroph.nnet.learning.SupervisedHebbianLearning
import org.neuroph.nnet.learning.UnsupervisedHebbianLearning
import org.neuroph.util.TransferFunctionType
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream
import java.util.*

/**
 * Created by Thomas Needham on 30/05/2016.
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
            Messages.showErrorDialog(OpenExistingNetworkConfigurationAction.project,"Error Reading Network From file", "Error")
            return null
        }
        catch (fnfe: FileNotFoundException){
            fnfe.printStackTrace(System.err)
            Messages.showErrorDialog(OpenExistingNetworkConfigurationAction.project, "No Network Configuration Found at: ${ShowTrainNetworkFormAction.properties.getValue(NETWORK_TO_TRAIN_LOCATION_KEY, "")}", "Error")
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
            TransferFunctions.Functions.GAUSSIAN -> function = TransferFunctionType.GAUSSIAN
            TransferFunctions.Functions.LINEAR -> function = TransferFunctionType.LINEAR
            TransferFunctions.Functions.LOG -> function = TransferFunctionType.LOG
            TransferFunctions.Functions.RAMP -> function = TransferFunctionType.RAMP
            TransferFunctions.Functions.SGN -> function = TransferFunctionType.SGN
            TransferFunctions.Functions.SIGMOID -> function = TransferFunctionType.SIGMOID
            TransferFunctions.Functions.STEP -> function = TransferFunctionType.STEP
            TransferFunctions.Functions.TANH -> function = TransferFunctionType.TANH
            TransferFunctions.Functions.TRAPEZOID -> function = TransferFunctionType.TRAPEZOID
            else -> UnknownTransferFunctionException("Unknown Transfer Function: ${TransferFunctions.GetClassName(networkConfiguration?.networkTransferFunction!!)}")
        }
        when (networkConfiguration?.networkType){
            NetworkTypes.Types.ADALINE -> network = Adaline(inputSize)
            NetworkTypes.Types.BAM -> BAM(inputSize,outputSize)
            NetworkTypes.Types.COMPETITIVE_NETWORK -> network = CompetitiveNetwork(inputSize,outputSize)
            NetworkTypes.Types.HOPFIELD -> network = Hopfield(inputSize + outputSize)
            NetworkTypes.Types.INSTAR -> network = Instar(inputSize)
            NetworkTypes.Types.KOHONEN -> network = Kohonen(inputSize,outputSize)
            NetworkTypes.Types.MAX_NET -> network = MaxNet(inputSize + outputSize)
            NetworkTypes.Types.MULTI_LAYER_PERCEPTRON -> network = MultiLayerPerceptron(networkConfiguration?.networkLayers?.toList(),function)
            NetworkTypes.Types.NEURO_FUZZY_PERCEPTRON -> network = NeuroFuzzyPerceptron(inputSize, Vector<Int>(inputSize), outputSize)
            NetworkTypes.Types.NEUROPH -> network = Perceptron(inputSize,outputSize,function)
            NetworkTypes.Types.OUTSTAR -> network = Outstar(outputSize)
            NetworkTypes.Types.PERCEPTRON -> network = Perceptron(inputSize,outputSize,function)
            NetworkTypes.Types.RBF_NETWORK -> network = RbfNetwork(inputSize,inputSize,outputSize)
            NetworkTypes.Types.SUPERVISED_HEBBIAN_NETWORK -> network = SupervisedHebbianNetwork(inputSize,outputSize,function)
            NetworkTypes.Types.UNSUPERVISED_HEBBIAN_NETWORK -> network = UnsupervisedHebbianNetwork(inputSize,outputSize,function)
            else -> UnknownNetworkTypeException("Unknown Network Type: ${NetworkTypes.GetClassName(networkConfiguration?.networkType!!)}")
            //TODO add more network types
        }
        when (networkConfiguration?.networkLearningRule){
            LearningRules.Rules.ANTI_HEBBAN_LEARNING -> network?.learningRule = AntiHebbianLearning()
            LearningRules.Rules.BACK_PROPAGATION -> network?.learningRule = BackPropagation()
            LearningRules.Rules.BINARY_DELTA_RULE -> network?.learningRule = BinaryDeltaRule()
            LearningRules.Rules.BINARY_HEBBIAN_LEARNING -> network?.learningRule = BinaryHebbianLearning()
            LearningRules.Rules.COMPETITIVE_LEARNING -> network?.learningRule = CompetitiveLearning()
            LearningRules.Rules.DYNAMIC_BACK_PROPAGATION -> network?.learningRule = DynamicBackPropagation()
            LearningRules.Rules.GENERALIZED_HEBBIAN_LEARNING -> network?.learningRule = GeneralizedHebbianLearning()
            LearningRules.Rules.HOPFIELD_LEARNING -> network?.learningRule = HopfieldLearning()
            LearningRules.Rules.INSTAR_LEARNING -> network?.learningRule = InstarLearning()
            LearningRules.Rules.KOHONEN_LEARNING -> network?.learningRule = KohonenLearning()
            LearningRules.Rules.LMS -> network?.learningRule = LMS()
            LearningRules.Rules.MOMENTUM_BACK_PROPAGATION -> network?.learningRule = MomentumBackpropagation()
            LearningRules.Rules.OJA_LEARNING -> network?.learningRule = OjaLearning()
            LearningRules.Rules.OUTSTAR_LEARNING -> network?.learningRule = OutstarLearning()
            LearningRules.Rules.PERCEPTRON_LEARNING -> network?.learningRule = PerceptronLearning()
            LearningRules.Rules.RESILIENT_PROPAGATION -> network?.learningRule = ResilientPropagation()
            LearningRules.Rules.SIGMOID_DELTA_RULE -> network?.learningRule = SigmoidDeltaRule()
            LearningRules.Rules.SIMULATED_ANNEALING_LEARNING -> network?.learningRule = SimulatedAnnealingLearning(network)
            LearningRules.Rules.SUPERVISED_HEBBIAN_LEARNING -> network?.learningRule = SupervisedHebbianLearning()
            LearningRules.Rules.UNSUPERVISED_HEBBIAN_LEARNING -> network?.learningRule = UnsupervisedHebbianLearning()
            else -> UnknownLearningRuleException("Unknown Learning Rule: ${LearningRules.GetClassName(networkConfiguration?.networkLearningRule!!)}")
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
        network?.calculate()
        network?.save(networkPath + ".nnet")
        return network
    }
}