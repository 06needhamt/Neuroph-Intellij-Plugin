/* The MIT License (MIT)

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
import com.thomas.needham.neurophidea.Constants
import com.thomas.needham.neurophidea.datastructures.LearningRules
import com.thomas.needham.neurophidea.datastructures.NetworkConfiguration
import com.thomas.needham.neurophidea.datastructures.NetworkTypes
import com.thomas.needham.neurophidea.datastructures.TransferFunctions
import com.thomas.needham.neurophidea.exceptions.UnknownLearningRuleException
import com.thomas.needham.neurophidea.exceptions.UnknownNetworkTypeException
import org.jetbrains.annotations.NotNull

/**
 * Created by thoma on 18/07/2016.
 */
class ScalaNetworkCodeGenerator : ICodeGenerator {
	val network: NetworkConfiguration?
	val outputPath: String
	var sourceOutput: String = ""
	private var braces: Int = 0
	
	companion object Data {
		@JvmStatic
		var properties = PropertiesComponent.getInstance()
		@JvmStatic
		var version = properties.getValue(Constants.VERSION_KEY)
		@JvmStatic
		var path: String? = ""
		val coreImports = "import java.io.BufferedReader" + "\n" +
				"import java.io.BufferedWriter" + "\n" +
				"import java.io.File" + "\n" +
				"import java.io.FileWriter" + "\n" +
				"import java.io.IOException" + "\n" +
				"import java.io.InputStreamReader" + "\n" +
				"import java.util" + "\n" +
				"import org.neuroph.core.NeuralNetwork" + "\n" +
				"import org.neuroph.core.learning.SupervisedTrainingElement" + "\n" +
				"import org.neuroph.core.learning.TrainingSet" + "\n" +
				"import org.neuroph.util.TransferFunctionType" + "\n" + "\n"
		
		val getLayersString: (Array<Int>) -> String = { layers ->
			//Use Some Kotlin Sorcery to convert Array<Int> to comma delimited string
			var result = ""
			for (i in IntRange(0, layers.size - 1)) {
				if (i == layers.size - 1)
					result += layers[i].toString()
				else
					result += (layers[i].toString() + ",")
			}
			result
		}
	}
	
	constructor(@NotNull network: NetworkConfiguration?, outputPath: String) {
		this.network = network
		this.outputPath = outputPath
		path = outputPath
	}
	
	fun GenerateCode(): String {
		sourceOutput += AddImports()
		sourceOutput += "\n" + "\n"
		sourceOutput += DefineObject()
		sourceOutput += "\n"
		sourceOutput += DefineGlobalVariables()
		sourceOutput += "\n"
		sourceOutput += DefineLoadNetwork()
		sourceOutput += "\n"
		sourceOutput += DefineTrainNetwork()
		sourceOutput += "\n"
		sourceOutput += DefineTestNetwork()
		sourceOutput += "\n"
		sourceOutput += DefineTestNetworkAuto()
		sourceOutput += "\n"
		sourceOutput += AddBraces()
		sourceOutput += "\n"
		return sourceOutput
	}
	
	private fun AddBraces(): String {
		var brace = ""
		while (braces > 0) {
			brace += "}" + "\n"
			braces--
		}
		return brace
	}
	
	private fun DefineTestNetworkAuto(): String {
		val testNetworkAuto = "def testNetworkAuto(setPath: String) { \n" +
				"var total : Double = 0.0 \n" +
				"val list = new util.ArrayList[Integer]() \n" +
				"val outputLine = new util.ArrayList[String]() \n" +
				"for (layer <- layers) { \n" +
				"list.add(layer) \n" +
				"} \n" +
				"testingSet = TrainingSet.createFromFile(setPath, inputSize, outputSize, \",\").asInstanceOf[TrainingSet[SupervisedTrainingElement]] \n" +
				"val count = testingSet.elements().size \n" +
				"var averageDeviance = 0.0 \n" +
				"var resultString = \"\" \n" +
				"try { \n" +
				"val file = new File(\"Results \" + setPath) \n" +
				"val fw = new FileWriter(file) \n" +
				"val bw = new BufferedWriter(fw) \n" +
				"for (i <- 0 until testingSet.elements().size) { \n" +
				"var expected: Double = 0.0 \n" +
				"var calculated: Double = 0.0 \n" +
				"network.setInput(testingSet.elementAt(i).getInput:_*) \n" +
				"network.calculate() \n" +
				"calculated = network.getOutput()(0) \n" +
				"expected = testingSet.elementAt(i).getIdealArray()(0) \n" +
				"println(\"Calculated Output: \" + calculated) \n" +
				"println(\"Expected Output: \" + expected) \n" +
				"println(\"Deviance: \" + (calculated - expected)) \n" +
				"averageDeviance += math.abs(math.abs(calculated) - math.abs(expected)) \n" +
				"total += network.getOutput()(0) \n" +
				"resultString = \"\" \n" +
				"for (cols <- testingSet.elementAt(i).getInputArray.indices) { \n " +
				"resultString += testingSet.elementAt(i).getInputArray()(cols) + \", \" \n" +
				"} \n" +
				"for (t <- network.getOutput.indices) { \n" +
				"resultString += network.getOutput()(t) + \", \" \n" +
				"} \n" +
				"resultString = resultString.substring(0, resultString.length - 2) \n" +
				"resultString += \"\" \n" +
				"bw.write(resultString) \n" +
				"bw.flush() \n" +
				"} \n" +
				"println() \n" +
				"println(\"Average: \" + total / count) \n" +
				"println(\"Average Deviance % : \" + (averageDeviance / count) * 100) \n" +
				"bw.flush() \n" +
				"bw.close() \n" +
				"} catch { \n" +
				"case ex: IOException => ex.printStackTrace() \n" +
				"} \n" +
				"} \n"
		return testNetworkAuto
	}
	
	private fun DefineTestNetwork(): String {
		val testNetwork = "def testNetwork() { \n" +
				"var input = \"\"\n" +
				"val fromKeyboard = new BufferedReader(new InputStreamReader(System.in)) \n" +
				"val testValues = new util.ArrayList[Double]() \n" +
				"var testValuesDouble: Array[Double] = null \n" +
				"do { \n" +
				"try { \n" +
				"println(\"Enter test values or \\\"\\\": \") \n" +
				"input = fromKeyboard.readLine() \n" +
				"if (input == \"\") { \n" +
				"//break \n" +
				"} \n" +
				"input = input.replace(\" \", \"\") \n" +
				"val stringVals = input.split(\",\") \n" +
				"testValues.clear() \n" +
				"for (value <- stringVals) { \n" +
				"testValues.add(value.toDouble) \n" +
				"}\n" +
				"} catch { \n" +
				"case ioe: IOException => ioe.printStackTrace(System.err) \n" +
				"case nfe: NumberFormatException => nfe.printStackTrace(System.err) \n" +
				"} \n" +
				"testValuesDouble = Array.ofDim[Double](testValues.size) \n" +
				"for (t <- testValuesDouble.indices) { \n" +
				"testValuesDouble(t) = testValues.get(t).doubleValue() \n" +
				"} \n" +
				"network.setInput(testValuesDouble:_*) \n" +
				"network.calculate() \n" +
				"} while (input != \"\") \n" +
				"} \n"
		return testNetwork
	}
	
	private fun DefineTrainNetwork(): String {
		val trainNetwork = "def trainNetwork() {\n" +
				"val list = new util.ArrayList[Integer]()\n" +
				"for (layer <- layers) {\n" +
				"list.add(layer)\n" +
				"}\n" +
				CreateNetwork(network?.networkType!!) + "\n" +
				"trainingSet = new TrainingSet[SupervisedTrainingElement](inputSize, outputSize)\n" +
				"trainingSet = TrainingSet.createFromFile(\"${network?.networkTrainingDataPath}\", inputSize, outputSize, \",\").asInstanceOf[TrainingSet[SupervisedTrainingElement]]\n" +
				CreateLearningRule(network?.networkLearningRule!!) + "\n" +
				"network.setLearningRule(learningRule)\n" +
				"network.learn(trainingSet)\n" +
				"network.save(\"${network?.networkOutputPath}" + "/" + "${network?.networkName}" + ".nnet\")" + "\n" +
				"}" + "\n"
		return trainNetwork
	}
	
	private fun DefineLoadNetwork(): String {
		val loadNetwork = "def loadNetwork() { \n " +
				"network = NeuralNetwork.load(\"${network?.networkOutputPath + "/" + network?.networkName + ".nnet"}\")" + "\n" + "}"
		return loadNetwork
	}
	
	private fun DefineGlobalVariables(): String {
		val variables = "  var inputSize : Int = ${network?.networkLayers?.first()} " +
				"\n  var outputSize : Int = ${network?.networkLayers?.last()}" +
				"\n  var network : NeuralNetwork = _" +
				"\n  var trainingSet : TrainingSet[SupervisedTrainingElement] = _" +
				"\n  var testingSet : TrainingSet[SupervisedTrainingElement] = _" +
				"\n  var layers : Array[Int] = Array(${getLayersString(network?.networkLayers!!)})"
		return variables
	}
	
	private fun DefineObject(): String {
		val classdef = "object ${network?.networkName} { "
		braces++
		return classdef
	}
	
	private fun CreateLearningRule(networkLearningRule: LearningRules.Rules): String {
		when (networkLearningRule) {
			LearningRules.Rules.ANTI_HEBBAN_LEARNING -> return "val learningRule = new AntiHebbianLearning();"
			LearningRules.Rules.BACK_PROPAGATION -> return "val learningRule = new BackPropagation();"
			LearningRules.Rules.BINARY_DELTA_RULE -> return "val learningRule = new BinaryDeltaRule();"
			LearningRules.Rules.BINARY_HEBBIAN_LEARNING -> return "val learningRule = new BinaryHebbianLearning();"
			LearningRules.Rules.COMPETITIVE_LEARNING -> return "val learningRule = new CompetitiveLearning();"
			LearningRules.Rules.DYNAMIC_BACK_PROPAGATION -> return "val learningRule = new DynamicBackPropagation();"
			LearningRules.Rules.GENERALIZED_HEBBIAN_LEARNING -> return "val learningRule = new GeneralizedHebbianLearning();"
			LearningRules.Rules.HOPFIELD_LEARNING -> return "val learningRule = new HopfieldLearning();"
			LearningRules.Rules.INSTAR_LEARNING -> return "val learningRule = new InstarLearning();"
			LearningRules.Rules.KOHONEN_LEARNING -> return "val learningRule = new KohonenLearning();"
			LearningRules.Rules.LMS -> return "val learningRule = new LMS();"
			LearningRules.Rules.MOMENTUM_BACK_PROPAGATION -> return "val learningRule = new MomentumBackpropagation();"
			LearningRules.Rules.OJA_LEARNING -> return "val learningRule = new OjaLearning();"
			LearningRules.Rules.OUTSTAR_LEARNING -> return "val learningRule = new OutstarLearning();"
			LearningRules.Rules.PERCEPTRON_LEARNING -> return "val learningRule = new PerceptronLearning();"
			LearningRules.Rules.RESILIENT_PROPAGATION -> return "val learningRule = new ResilientPropagation();"
			LearningRules.Rules.SIGMOID_DELTA_RULE -> return "val learningRule = new SigmoidDeltaRule();"
			LearningRules.Rules.SIMULATED_ANNEALING_LEARNING -> return "val learningRule = new SimulatedAnnealingLearning(network);"
			LearningRules.Rules.SUPERVISED_HEBBIAN_LEARNING -> return "val learningRule = new SupervisedHebbianLearning();"
			LearningRules.Rules.UNSUPERVISED_HEBBIAN_LEARNING -> return "val learningRule = new UnsupervisedHebbianLearning();"
			else -> UnknownLearningRuleException("Unknown Learning Rule: ${LearningRules.GetClassName(NetworkTrainer.networkConfiguration?.networkLearningRule!!)}")
		}
		return ""
	}
	
	private fun CreateNetwork(networkType: NetworkTypes.Types): String {
		when (networkType) {
			NetworkTypes.Types.ADALINE -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize);"
			NetworkTypes.Types.BAM -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize, outputSize);"
			NetworkTypes.Types.COMPETITIVE_NETWORK -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize, outputSize);"
			NetworkTypes.Types.HOPFIELD -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize + outputSize);"
			NetworkTypes.Types.INSTAR -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize);"
			NetworkTypes.Types.KOHONEN -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize, outputSize);"
			NetworkTypes.Types.MAX_NET -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize + outputSize);"
			NetworkTypes.Types.MULTI_LAYER_PERCEPTRON -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(list, TransferFunctionType.${network?.networkTransferFunction?.name});"
			NetworkTypes.Types.NEURO_FUZZY_PERCEPTRON -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize, new util.Vector[Integer](inputSize), outputSize);"
			NetworkTypes.Types.NEUROPH -> return "network = new Perceptron(inputSize, outputSize, TransferFunctionType.${network?.networkTransferFunction?.name});"
			NetworkTypes.Types.OUTSTAR -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(outputSize);"
			NetworkTypes.Types.PERCEPTRON -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize, outputSize, TransferFunctionType.${network?.networkTransferFunction?.name});"
			NetworkTypes.Types.RBF_NETWORK -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize, inputSize, outputSize);"
			NetworkTypes.Types.SUPERVISED_HEBBIAN_NETWORK -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize, outputSize, TransferFunctionType.${network?.networkTransferFunction?.name});"
			NetworkTypes.Types.UNSUPERVISED_HEBBIAN_NETWORK -> return "network = new ${NetworkTypes.GetClassName(networkType)}" + "(inputSize, outputSize, TransferFunctionType.${network?.networkTransferFunction?.name});"
			else -> UnknownNetworkTypeException("Unknown Network Type: ${NetworkTypes.GetClassName(NetworkTrainer.networkConfiguration?.networkType!!)}")
		}
		return ""
	}
	
	private fun AddImports(): String {
		var imports = coreImports
		when (network?.networkType) {
			NetworkTypes.Types.PERCEPTRON -> imports += "import org.neuroph.nnet.Perceptron" + "\n"
			NetworkTypes.Types.MULTI_LAYER_PERCEPTRON -> imports += "import org.neuroph.nnet.MultiLayerPerceptron" + "\n"
			NetworkTypes.Types.ADALINE -> imports += "import org.neuroph.nnet.Adaline" + "\n"
			NetworkTypes.Types.BAM -> imports += "import org.neuroph.nnet.BAM" + "\n"
			NetworkTypes.Types.COMPETITIVE_NETWORK -> imports += "import org.neuroph.nnet.CompetitiveNetwork" + "\n"
			NetworkTypes.Types.HOPFIELD -> imports += "import org.neuroph.nnet.Hopfield" + "\n"
			NetworkTypes.Types.INSTAR -> imports += "import org.neuroph.nnet.Instar" + "\n"
			NetworkTypes.Types.KOHONEN -> imports += "import org.neuroph.nnet.Kohonen" + "\n"
			NetworkTypes.Types.MAX_NET -> imports += "import org.neuroph.nnet.MaxNet" + "\n"
			NetworkTypes.Types.NEUROPH -> imports += "import org.neuroph.nnet.Perceptron" + "\n"
			NetworkTypes.Types.NEURO_FUZZY_PERCEPTRON -> imports += "import org.neuroph.nnet.NeuroFuzzyPerceptron" + "\n"
			NetworkTypes.Types.OUTSTAR -> imports += "import org.neuroph.nnet.Outstar" + "\n"
			NetworkTypes.Types.RBF_NETWORK -> imports += "import org.neuroph.nnet.RbfNetwork" + "\n"
			NetworkTypes.Types.SUPERVISED_HEBBIAN_NETWORK -> imports += "import org.neuroph.nnet.SupervisedHebbianNetwork" + "\n"
			NetworkTypes.Types.UNSUPERVISED_HEBBIAN_NETWORK -> imports += "import org.neuroph.nnet.UnsupervisedHebbianNetwork" + "\n"
			else -> imports += "import UnknownType" + "\n"
		}
		when (network?.networkLearningRule) {
			LearningRules.Rules.BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.BackPropagation" + "\n"
			LearningRules.Rules.DYNAMIC_BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.DynamicBackPropagation" + "\n"
			LearningRules.Rules.ANTI_HEBBAN_LEARNING -> imports += "import org.neuroph.nnet.learning.AntiHebbianLearning" + "\n"
			LearningRules.Rules.BINARY_DELTA_RULE -> imports += "import org.neuroph.nnet.learning.BinaryDeltaRule" + "\n"
			LearningRules.Rules.BINARY_HEBBIAN_LEARNING -> imports += "import org.neuroph.nnet.learning.BinaryHebbianLearning" + "\n"
			LearningRules.Rules.COMPETITIVE_LEARNING -> imports += "import org.neuroph.nnet.learning.CompetitiveLearning" + "\n"
			LearningRules.Rules.GENERALIZED_HEBBIAN_LEARNING -> imports += "import org.neuroph.nnet.learning.GeneralizedHebbianLearning" + "\n"
			LearningRules.Rules.HOPFIELD_LEARNING -> imports += "import org.neuroph.nnet.learning.HopfieldLearning" + "\n"
			LearningRules.Rules.INSTAR_LEARNING -> imports += "import org.neuroph.nnet.learning.InstarLearning" + "\n"
			LearningRules.Rules.KOHONEN_LEARNING -> imports += "import org.neuroph.nnet.learning.KohonenLearning" + "\n"
			LearningRules.Rules.LMS -> imports += "import org.neuroph.nnet.learning.LMS" + "\n"
			LearningRules.Rules.MOMENTUM_BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.MomentumBackPropagation" + "\n"
			LearningRules.Rules.OJA_LEARNING -> imports += "import org.neuroph.nnet.learning.OjaLearning" + "\n"
			LearningRules.Rules.OUTSTAR_LEARNING -> imports += "import org.neuroph.nnet.learning.OutstarLearning" + "\n"
			LearningRules.Rules.PERCEPTRON_LEARNING -> imports += "import org.neuroph.nnet.learning.PerceptronLearning" + "\n"
			LearningRules.Rules.RESILIENT_PROPAGATION -> imports += "import org.neuroph.nnet.learning.ResilientPropagation" + "\n"
			LearningRules.Rules.SIGMOID_DELTA_RULE -> imports += "import org.neuroph.nnet.learning.SigmoidDeltaRule" + "\n"
			LearningRules.Rules.SIMULATED_ANNEALING_LEARNING -> imports += "import org.neuroph.nnet.learning.SimulatedAnnealingLearning" + "\n"
			LearningRules.Rules.SUPERVISED_HEBBIAN_LEARNING -> imports += "import org.neuroph.nnet.learning.SupervisedHebbianLearning" + "\n"
			LearningRules.Rules.UNSUPERVISED_HEBBIAN_LEARNING -> imports += "import org.neuroph.nnet.learning.UnsupervisedHebbianLearning" + "\n"
			else -> imports += "import UnknownLearningRule" + "\n"
		}
		when (network?.networkTransferFunction) {
			TransferFunctions.Functions.GAUSSIAN -> imports += "import org.neuroph.core.transfer.Gaussian" + "\n"
			TransferFunctions.Functions.SIGMOID -> imports += "import org.neuroph.core.transfer.Sigmoid" + "\n"
			TransferFunctions.Functions.LINEAR -> imports += "import org.neuroph.core.transfer.Linear" + "\n"
			TransferFunctions.Functions.LOG -> imports += "import org.neuroph.core.transfer.Log" + "\n"
			TransferFunctions.Functions.RAMP -> imports += "import org.neuroph.core.transfer.Ramp" + "\n"
			TransferFunctions.Functions.TRAPEZOID -> imports += "import org.neuroph.core.transfer.Trapezoid" + "\n"
			TransferFunctions.Functions.SGN -> imports += "import org.neuroph.core.transfer.Sgn" + "\n"
			TransferFunctions.Functions.SIN -> imports += "import org.neuroph.core.transfer.Sin" + "\n"
			TransferFunctions.Functions.STEP -> imports += "import org.neuroph.core.transfer.Step" + "\n"
			TransferFunctions.Functions.TANH -> imports += "import org.neuroph.core.transfer.Tanh" + "\n"
			else -> imports += "import UnknownTransferFunction" + "\n"
		}
		return imports
	}
}