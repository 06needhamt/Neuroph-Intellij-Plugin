package com.thomas.needham.neurophidea.datastructures

import com.intellij.ide.util.PropertiesComponent
import com.thomas.needham.neurophidea.Constants
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
        @JvmStatic var version = properties.getValue(Constants.VERSION_KEY)
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
        sourceOutput += DefineTestNetwork();
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
        }
        return brace
    }

    private fun DefineTestNetworkAuto() : String {
        val testNetworkAuto = "static void testNetworkAuto(String setPath) { " + "\n" +
                                "double total = 0.0;" + "\n" +
                                "ArrayList<Integer> list = new ArrayList<Integer>" + "\n" +
                                "ArrayList<String> outputLine = new ArrayList<String>();" + "\n" +
                                "for(int layer : layers) { " + "\n" +
                                "list.add(layer);" + "\n" +
                                "}" + "\n" + "\n" +
                                "testingSet = TrainingSet.createFromFile(setpath, inputSize, outputSize,\",\"" + "\n" +
                                "int count = testingset.elements().size();" + "\n" +
                                "double averageDevience = 0;" + "\n" +
                                "String resultString = \"\";" + "\n" +
                                "try{" + "\n" +
                                "File file = new File(\"Results \" + setName);" + "\n" +
                                "FileWriter fw = new FileWriter(file);" + "\n" +
                                "BufferedWriter bw = new BufferedWriter(fw);" + "\n" +
                                "" + "\n" +
                                "for(int i = 0; i < testingset.elements().size(); i ++){" + "\n" +
                                "double expected;" + "\n" +
                                "double calculated;" + "\n" +
                                "network.setInput(testingset.elementAt(i).getInput());" + "\n" +
                                "network.calculate();" + "\n" +
                                "calculated = network.getOutput()[0];" + "\n" +
                                "expected = testingset.elementAt(i).getIdealArray()[0];" + "\n" +
                                "System.out.println(\"Calculated Output: \" + calculated);" + "\n" +
                                "System.out.println(\"Expected Output: \" + expected);" + "\n" +
                                "System.out.println(\"Devience: \" + (calculated - expected));" + "\n" +
                                "averageDevience += Math.abs(Math.abs(calculated) - Math.abs(expected));" + "\n" +
                                "total += network.getOutput()[0];" + "\n" +
                                "resultString = \";\" "+ "\n" +
                                "for(int cols = 0; cols < testingset.elementAt(i).getInputArray().length; cols ++) {" + "\n" +
                                "resultString += testingset.elementAt(i).getInputArray()[cols] + \", \";" + "\n" +
                                "}" + "\n" +
                                "for(int t = 0; t < network.getOutput().length; t++) {" + "\n" +
                                "resultString += network.getOutput()[t] + \", \";" + "\n" +
                                "}" + "\n" +
                                "resultString = resultString.substring(0, resultString.length()-2);" + "\n" +
                                "resultString += \"\n\";" + "\n" +
                                "bw.write(resultString);" + "\n" +
                                "bw.flush();" + "\n" +
                                "}" + "\n" +
                                "System.out.println();" + "\n" +
                                "System.out.println(\"Average: \" + total / count);" + "\n" +
                                "System.out.println(\"Average Devience % : \" + (averageDevience / count) * 100);" + "\n" +
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
                            "BufferredReader fromKeyboard = new BufferredReader(new InputStreamReader(System.in));" + "\n" +
                            "ArrayList<Double> testValues = new ArrayList<Double>();" + "\n" +
                            "double[] testValuesDouble;" + "\n" +
                            "do { " + "\n" +
                            "try { " + "\n" +
                            "System.out.println(\"Enter test values or \"\": \");" + "\n" +
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
                            "testValuesDouble = new Double[testValues.size()];" + "\n" +
                            "for(int t = 0; t < testValuesDouble.length; t++) {" + "\n" +
                            "testValuesDouble[t] = testValues.get(t).doubleValue();" + "\n" +
                            "}" + "\n" +
                            "network.setInput(testValuesDouble);" + "\n" +
                            "network.calculate();" + "\n" +
                            "printOutput(network.getOutput());" + "\n" +
                            "} while (!input.equals(\"\"));" + "\n" +
                            "}" + "\n"
        return testNetwork
    }

    private fun DefineTrainNetwork() : String {
        val trainNetwork = "static void trainNetwork() { " + "\n" +
                            "ArrayList<Integer> list = new ArrayList<Integer>" + "\n" +
                            "for(int layer : layers) { " + "\n" +
                            "list.add(layer);" + "\n" +
                            "}" + "\n" + "\n" +
                            "NeuralNetwork network = new ${NetworkTypes.GetClassName(network?.networkType!!)}" +
                            "(list, TransferFunctionType.${network?.networkTransferFunction?.name});" + "\n" +
                            "trainingSet = new TrainingSet<SupervisedLearningElement>(inputSize, outputSize);" + "\n" +
                            "trainingSet = TrainingSet.createFromFile(${network?.networkTrainingDataPath}, inputSize, outputSize, \",\");" + "\n" +
                            "${TransferFunctions.GetClassName(network?.networkTransferFunction!!)} learningRule = " +
                            "new ${TransferFunctions.GetClassName(network?.networkTransferFunction!!)}();" + "\n" +
                            "network.setLearningRule(learningRule);" + "\n" +
                            "network.learn(trainingSet);" + "\n" +
                            "network.save(${network?.networkOutputPath}" + "/" + "${network?.networkName}" + ".nnet);" + "\n" +
                            "}" + "\n"
        return trainNetwork
    }

    private fun DefineLoadNetwork() : String {
        val loadNetwork = "static void loadNetwork() { " + "\n" +
                                "network = NeuralNetwork.load(${network?.networkOutputPath + "/" + network?.networkName + ".conf"};" + "\n" +
                            "}" + "\n"
        return loadNetwork
    }

    private fun DefineGlobalVariables() : String {
        val variables = "static int inputSize = ${network?.networkLayers?.first()};" + "\n" +
                        "static int outputSize = ${network?.networkLayers?.last()};" + "\n" +
                        "static NeuralNetwork network;" + "\n" +
                        "static TrainingSet<SupervisedTrainingElement> trainingSet" + "\n" +
                        "static TrainingSet<SupervisedTrainingElement> testingSet" + "\n" +
                        "static int[] layers = ${network?.networkLayers}" + "\n"
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
            NetworkTypes.Types.MULTI_LAYER_PERCEPTRON -> imports += "org.neuroph.nnet.MultiLayerPerceptron" + "\n"
            else -> imports += "import UnknownType;" + "\n"
            //TODO Add More Network Types
        }
        when(network?.networkLearningRule){
            LearningRules.Rules.BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.BackPropagation;" + "\n"
            LearningRules.Rules.DYNAMIC_BACK_PROPAGATION -> imports += "import org.neuroph.nnet.learning.DynamicBackPropagation;" + "\n"
            else -> imports += "import UnknownLearningRule;" + "\n"
        }
        when(network?.networkTransferFunction){
            TransferFunctions.Functions.GAUSSIAN -> imports += "import org.neuroph.core.transfer.Gaussian" + "\n"
            TransferFunctions.Functions.SIGMOID -> imports += "import org.neuroph.core.transfer.Sigmoid" + "\n"
            else -> imports += "import UnknownTransferFunction;" + "\n"
        }
        return imports
    }
}