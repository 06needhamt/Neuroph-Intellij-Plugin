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
import com.thomas.needham.neurophidea.Constants.VERSION_KEY
import com.thomas.needham.neurophidea.datastructures.LearningRules
import com.thomas.needham.neurophidea.datastructures.NetworkConfiguration
import com.thomas.needham.neurophidea.datastructures.NetworkTypes
import com.thomas.needham.neurophidea.datastructures.TransferFunctions
import org.jetbrains.annotations.NotNull

/**
 * Created by Thomas Needham on 18/07/2016.
 */
class GroovyNetworkCodeGenerator : ICodeGenerator {
    val network : NetworkConfiguration?
    val outputPath : String
    var sourceOutput : String = ""
    private var braces : Int = 0

    companion object Data{
        @JvmStatic var properties = PropertiesComponent.getInstance()
        @JvmStatic var version = properties.getValue(VERSION_KEY)
        @JvmStatic var path : String? = ""
        val coreImports = "import org.neuroph.core.NeuralNetwork;" + "\n" +
                "import org.neuroph.core.learning.SupervisedTrainingElement;" + "\n" +
                "import org.neuroph.core.learning.TrainingSet;" + "\n" +
                "import org.neuroph.util.TransferFunctionType;" + "\n" + "\n"
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
    }

    constructor(@NotNull network: NetworkConfiguration?, outputPath: String){
        this.network = network
        this.outputPath = outputPath
        path = outputPath
    }

    fun GenerateCode() : String{
        sourceOutput += AddImports()
        sourceOutput += "\n" + "\n"
        sourceOutput += DefineClass()
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
        sourceOutput += AddLooperClass()
        sourceOutput += "\n"
        sourceOutput += AddBraces()
        sourceOutput += "\n"
        return sourceOutput
    }

    private fun  AddLooperClass() : String {
        val definition = "static class Looper { " + "\n" +
                "private Closure code" + "\n" + "\n" +
                "static Looper loop(Closure code) { " + "\n" +
                "new Looper(code:code);" + "\n" +
                "}" + "\n" + "\n" +
                "void until(Closure test) { " + "\n" +
                "code();" + "\n" +
                "while (!test()) { " + "\n" +
                "code();" + "\n" +
                "}" + "\n" +
                "}" + "\n" +
                "}" + "\n"
        return definition
    }

    private fun AddBraces() : String {
        var brace = ""
        while(braces > 0){
            brace += "}" + "\n"
            braces--
        }
        return brace
    }

    private fun DefineTestNetworkAuto() : String {
        val testNetworkAuto = "static void testNetworkAuto(String setPath) { " + "\n" +
                "double total = 0.0;" + "\n" +
                "ArrayList<Integer> list = new ArrayList<Integer>();" + "\n" +
                "ArrayList<String> outputLine = new ArrayList<String>();" + "\n" +
                "for(int layer : layers) { " + "\n" +
                "list.add(layer);" + "\n" +
                "}" + "\n" + "\n" +
                "testingSet = TrainingSet.createFromFile(setPath, inputSize, outputSize,\",\");" + "\n" +
                "int count = testingSet.elements().size();" + "\n" +
                "double averageDeviance = 0;" + "\n" +
                "String resultString = \"\";" + "\n" +
                "try{" + "\n" +
                "File file = new File(\"Results \" + setPath);" + "\n" +
                "FileWriter fw = new FileWriter(file);" + "\n" +
                "BufferedWriter bw = new BufferedWriter(fw);" + "\n" +
                "" + "\n" +
                "for(int i = 0; i < testingSet.elements().size(); i ++){" + "\n" +
                "double expected;" + "\n" +
                "double calculated;" + "\n" +
                "network.setInput(testingSet.elementAt(i).getInput());" + "\n" +
                "network.calculate();" + "\n" +
                "calculated = network.getOutput()[0];" + "\n" +
                "expected = testingSet.elementAt(i).getIdealArray()[0];" + "\n" +
                "System.out.println(\"Calculated Output: \" + calculated);" + "\n" +
                "System.out.println(\"Expected Output: \" + expected);" + "\n" +
                "System.out.println(\"Deviance: \" + (calculated - expected));" + "\n" +
                "averageDeviance += Math.abs(Math.abs(calculated) - Math.abs(expected));" + "\n" +
                "total += network.getOutput()[0];" + "\n" +
                "resultString = \"\";"+ "\n" +
                "for(int cols = 0; cols < testingSet.elementAt(i).getInputArray().length; cols++) {" + "\n" +
                "resultString += testingSet.elementAt(i).getInputArray()[cols] + \", \";" + "\n" +
                "}" + "\n" +
                "for(int t = 0; t < network.getOutput().length; t++) {" + "\n" +
                "resultString += network.getOutput()[t] + \", \";" + "\n" +
                "}" + "\n" +
                "resultString = resultString.substring(0, resultString.length()-2);" + "\n" +
                "resultString += \"\";" + "\n" +
                "bw.write(resultString);" + "\n" +
                "bw.flush();" + "\n" +
                "}" + "\n" +
                "System.out.println();" + "\n" +
                "System.out.println(\"Average: \" + total / count);" + "\n" +
                "System.out.println(\"Average Deviance % : \" + (averageDeviance / count) * 100);" + "\n" +
                "bw.flush();" + "\n" +
                "bw.close();" + "\n" +
                "}" + "\n" +
                "catch(IOException ex) {" + "\n" +
                "ex.printStackTrace();" + "\n" +
                "}" + "\n" +
                "}" + "\n"
        return testNetworkAuto;
    }

    private fun DefineTestNetwork() : String {
        val testNetwork = "static void testNetwork() { " + "\n" +
                "String input = \"\";" + "\n" +
                "BufferedReader fromKeyboard = new BufferedReader(new InputStreamReader(System.in));" + "\n" +
                "ArrayList<Double> testValues = new ArrayList<Double>();" + "\n" +
                "double[] testValuesDouble;" + "\n" +
                "Looper.loop { " + "\n" +
                "try { " + "\n" +
                "System.out.println(\"Enter test values or \\\"\\\": \");" + "\n" +
                "input = fromKeyboard.readLine();" + "\n" +
                "if(input == \"\") { " + "\n" +
                "return;" + "\n" +
                "}" + "\n" +
                "input = input.replace(\" \", \"\");" + "\n" +
                "String[] stringVals = input.split(\",\");" + "\n" +
                "testValues.clear();" + "\n" +
                "for(String val : stringVals) { " + "\n" +
                "testValues.add(Double.parseDouble(val));" + "\n" +
                "}" + "\n" +
                "} catch(IOException ioe) { " + "\n" +
                "ioe.printStackTrace(System.err);" + "\n" +
                "} catch(NumberFormatException nfe) { " + "\n" +
                "nfe.printStackTrace(System.err);" + "\n" +
                "}" + "\n" +
                "testValuesDouble = new double[testValues.size()];" + "\n" +
                "for(int t = 0; t < testValuesDouble.length; t++) {" + "\n" +
                "testValuesDouble[t] = testValues.get(t).doubleValue();" + "\n" +
                "}" + "\n" +
                "network.setInput(testValuesDouble);" + "\n" +
                "network.calculate();" + "\n" +
                "} until (input != \"\");" + "\n" +
                "}" + "\n"
        return testNetwork
    }

    private fun DefineTrainNetwork() : String {
        val trainNetwork = "static void trainNetwork() { " + "\n" +
                "ArrayList<Integer> list = new ArrayList<Integer>();" + "\n" +
                "for(int layer : layers) { " + "\n" +
                "list.add(layer);" + "\n" +
                "}" + "\n" + "\n" +
                "network = new ${NetworkTypes.GetClassName(network?.networkType!!)}" +
                "(list, TransferFunctionType.${network?.networkTransferFunction?.name});" + "\n" +
                "trainingSet = new TrainingSet<SupervisedTrainingElement>(inputSize, outputSize);" + "\n" +
                "trainingSet = TrainingSet.createFromFile(\"${network?.networkTrainingDataPath}\", inputSize, outputSize, \",\");" + "\n" +
                "${LearningRules.GetClassName(network?.networkLearningRule!!)} learningRule = " +
                "new ${LearningRules.GetClassName(network?.networkLearningRule!!)}();" + "\n" +
                "network.setLearningRule(learningRule);" + "\n" +
                "network.learn(trainingSet);" + "\n" +
                "network.save(\"${network?.networkOutputPath}" + "/" + "${network?.networkName}" + ".nnet\");" + "\n" +
                "}" + "\n"
        return trainNetwork
    }

    private fun DefineLoadNetwork() : String {
        val loadNetwork = "static void loadNetwork() { " + "\n" +
                "network = NeuralNetwork.load(\"${network?.networkOutputPath + "/" + network?.networkName + ".nnet"}\");" + "\n" +
                "}" + "\n"
        return loadNetwork
    }

    private fun DefineGlobalVariables() : String {
        val variables = "static int inputSize = ${network?.networkLayers?.first()};" + "\n" +
                "static int outputSize = ${network?.networkLayers?.last()};" + "\n" +
                "static network;" + "\n" +
                "static TrainingSet<SupervisedTrainingElement> trainingSet;" + "\n" +
                "static TrainingSet<SupervisedTrainingElement> testingSet;" + "\n" +
                "static int[] layers = [${getLayersString(network?.networkLayers!!)}];" + "\n"
        return variables
    }

    private fun DefineClass() : String {
        val classdef = "public class ${network?.networkName} { "
        braces++
        return classdef
    }

    private fun AddImports() : String {
        var imports = coreImports
        when (network?.networkType){
            NetworkTypes.Types.PERCEPTRON -> imports += "import org.neuroph.nnet.Perceptron;" + "\n"
            NetworkTypes.Types.MULTI_LAYER_PERCEPTRON -> imports += "import org.neuroph.nnet.MultiLayerPerceptron;" + "\n"
            NetworkTypes.Types.ADALINE -> imports += "import org.neuroph.nnet.Adaline;" + "\n"
            NetworkTypes.Types.BAM -> imports += "import org.neuroph.nnet.BAM;" + "\n"
            NetworkTypes.Types.COMPETITIVE_NETWORK -> imports += "import org.neuroph.nnet.CompetitiveNetwork;" + "\n"
            NetworkTypes.Types.HOPFIELD -> imports += "import org.neuroph.nnet.Hopfield;" + "\n"
            NetworkTypes.Types.INSTAR -> imports += "import org.neuroph.nnet.Instar;" + "\n"
            NetworkTypes.Types.KOHONEN -> imports += "import org.neuroph.nnet.Kohonen;" + "\n"
            NetworkTypes.Types.MAX_NET -> imports += "import org.neuroph.nnet.MaxNet;" + "\n"
            NetworkTypes.Types.NEUROPH -> imports += "import org.neuroph.nnet.Neuroph;" + "\n"
            NetworkTypes.Types.NEURO_FUZZY_PERCEPTRON -> imports += "import org.neuroph.nnet.NeuroFuzzyPerceptron;" + "\n"
            NetworkTypes.Types.OUTSTAR -> imports += "import org.neuroph.nnet.OutStar;" + "\n"
            NetworkTypes.Types.RBF_NETWORK -> imports += "import org.neuroph.nnet.RbfNetwork;" + "\n"
            NetworkTypes.Types.SUPERVISED_HEBBIAN_NETWORK -> imports += "import org.neuroph.nnet.SupervisedHebbianNetwork;" + "\n"
            NetworkTypes.Types.UNSUPERVISED_HEBBIAN_NETWORK -> imports += "import org.neuroph.nnet.UnsupervisedHebbianNetwork;" + "\n"
            else -> imports += "import UnknownType;" + "\n"
        }
        when(network?.networkLearningRule){
            LearningRules.Rules.BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.BackPropagation;" + "\n"
            LearningRules.Rules.DYNAMIC_BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.DynamicBackPropagation;" + "\n"
            LearningRules.Rules.ANTI_HEBBAN_LEARNING -> imports += "import org.neuroph.nnet.learning.AntiHebbianLearning;" + "\n"
            LearningRules.Rules.BINARY_DELTA_RULE -> imports += "import org.neuroph.nnet.learning.BinaryDeltaRule;" + "\n"
            LearningRules.Rules.BINARY_HEBBIAN_LEARNING -> imports += "import org.neuroph.nnet.learning.BinaryHebbianLearning;" + "\n"
            LearningRules.Rules.COMPETITIVE_LEARNING -> imports += "import org.neuroph.nnet.learning.CompetitiveLearning" + "\n"
            LearningRules.Rules.GENERALIZED_HEBBIAN_LEARNING -> imports += "import org.neuroph.nnet.learning.GeneralizedHebbianLearning;" + "\n"
            LearningRules.Rules.HOPFIELD_LEARNING -> imports += "import org.neuroph.nnet.learning.HopfieldLearning;" + "\n"
            LearningRules.Rules.INSTAR_LEARNING -> imports += "import org.neuroph.nnet.learning.InstarLearning;" + "\n"
            LearningRules.Rules.KOHONEN_LEARNING -> imports += "import org.neuroph.nnet.learning.KohonenLearning;" + "\n"
            LearningRules.Rules.LMS -> imports += "import org.neuroph.nnet.learning.LMS;" + "\n"
            LearningRules.Rules.MOMENTUM_BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.MomentumBackPropagation;" + "\n"
            LearningRules.Rules.OJA_LEARNING -> imports += "import org.neuroph.nnet.learning.OjaLearning;" + "\n"
            LearningRules.Rules.OUTSTAR_LEARNING -> imports += "import org.neuroph.nnet.learning.OutstarLearning;" + "\n"
            LearningRules.Rules.PERCEPTRON_LEARNING -> imports += "import org.neuroph.nnet.learning.PerceptronLearning;" + "\n"
            LearningRules.Rules.RESILIENT_PROPAGATION -> imports += "import org.neuroph.nnet.learning.ResilientPropagation;" + "\n"
            LearningRules.Rules.SIGMOID_DELTA_RULE -> imports += "import org.neuroph.nnet.learning.SigmoidDeltaRule;" + "\n"
            LearningRules.Rules.SIMULATED_ANNEALING_LEARNING -> imports += "import org.neuroph.nnet.learning.SimulatedAnnealingLearning;" + "\n"
            LearningRules.Rules.SUPERVISED_HEBBIAN_LEARNING -> imports += "import org.neuroph.nnet.learning.SupervisedHebbianLearning;" + "\n"
            LearningRules.Rules.UNSUPERVISED_HEBBIAN_LEARNING -> imports += "import org.neuroph.nnet.learning.UnsupervisedHebbianLearning;" + "\n"
            else -> imports += "import UnknownLearningRule;" + "\n"
        }
        when(network?.networkTransferFunction){
            TransferFunctions.Functions.GAUSSIAN -> imports += "import org.neuroph.core.transfer.Gaussian;" + "\n"
            TransferFunctions.Functions.SIGMOID -> imports += "import org.neuroph.core.transfer.Sigmoid;" + "\n"
            TransferFunctions.Functions.LINEAR -> imports += "import org.neuroph.core.transfer.Linear;" + "\n"
            TransferFunctions.Functions.LOG -> imports += "import org.neuroph.core.transfer.Log;" + "\n"
            TransferFunctions.Functions.RAMP -> imports += "import org.neuroph.core.transfer.Ramp;" + "\n"
            TransferFunctions.Functions.TRAPEZOID -> imports += "import org.neuroph.core.transfer.Trapezoid;" + "\n"
            TransferFunctions.Functions.SGN -> imports += "import org.neuroph.core.transfer.Sgn;" + "\n"
            TransferFunctions.Functions.SIN -> imports += "import org.neuroph.core.transfer.Sin;" + "\n"
            TransferFunctions.Functions.STEP -> imports += "import org.neuroph.core.transfer.Step;" + "\n"
            TransferFunctions.Functions.TANH -> imports += "import org.neuroph.core.transfer.Tanh;" + "\n"
            else -> imports += "import UnknownTransferFunction;" + "\n"
        }
        return imports
    }
}