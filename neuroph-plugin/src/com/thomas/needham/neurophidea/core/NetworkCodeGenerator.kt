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
 * Created by thoma on 28/05/2016.
 */
class NetworkCodeGenerator {
    val network : NetworkConfiguration?
    val outputPath : String
    var sourceOutput : String = ""
    private var braces : Int = 0

    companion object Data{
        @JvmStatic var properties = PropertiesComponent.getInstance()
        @JvmStatic var version = properties.getValue(VERSION_KEY)
        @JvmStatic var path : String? = ""
        val coreImports = "import java.io.BufferedReader;" + "\n" +
                            "import java.io.BufferedWriter;" + "\n" +
                            "import java.io.File;" + "\n" +
                            "import java.io.FileWriter;" + "\n" +
                            "import java.io.IOException;" + "\n" +
                            "import java.io.InputStreamReader;" + "\n" +
                            "import java.util.ArrayList;" + "\n" +
                            "import java.util.logging.Level;" + "\n" +
                            "import java.util.logging.Logger;" + "\n" +
                            "import org.neuroph.core.NeuralNetwork;" + "\n" +
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
        sourceOutput += AddBraces()
        sourceOutput += "\n"
        return sourceOutput
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
                            "do { " + "\n" +
                            "try { " + "\n" +
                            "System.out.println(\"Enter test values or \\\"\\\": \");" + "\n" +
                            "input = fromKeyboard.readLine();" + "\n" +
                            "if(input.equals(\"\")) { " + "\n" +
                            "break;" + "\n" +
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
                            "} while (!input.equals(\"\"));" + "\n" +
                            "}" + "\n"
        return testNetwork
    }

    private fun DefineTrainNetwork() : String {
        val trainNetwork = "static void trainNetwork() { " + "\n" +
                            "ArrayList<Integer> list = new ArrayList<Integer>();" + "\n" +
                            "for(int layer : layers) { " + "\n" +
                            "list.add(layer);" + "\n" +
                            "}" + "\n" + "\n" +
                            "NeuralNetwork network = new ${NetworkTypes.GetClassName(network?.networkType!!)}" +
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
                        "static NeuralNetwork network;" + "\n" +
                        "static TrainingSet<SupervisedTrainingElement> trainingSet;" + "\n" +
                        "static TrainingSet<SupervisedTrainingElement> testingSet;" + "\n" +
                        "static int[] layers = {${getLayersString(network?.networkLayers!!)}};" + "\n"
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
            else -> imports += "import UnknownType;" + "\n"
            //TODO Add More Network Types
        }
        when(network?.networkLearningRule){
            LearningRules.Rules.BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.BackPropagation;" + "\n"
            LearningRules.Rules.DYNAMIC_BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.DynamicBackPropagation;" + "\n"
            else -> imports += "import UnknownLearningRule;" + "\n"
            //TODO Add More Learning Rules
        }
        when(network?.networkTransferFunction){
            TransferFunctions.Functions.GAUSSIAN -> imports += "import org.neuroph.core.transfer.Gaussian;" + "\n"
            TransferFunctions.Functions.SIGMOID -> imports += "import org.neuroph.core.transfer.Sigmoid;" + "\n"
            else -> imports += "import UnknownTransferFunction;" + "\n"
            //TODO Add More Transfer Functions
        }
        return imports
    }
}