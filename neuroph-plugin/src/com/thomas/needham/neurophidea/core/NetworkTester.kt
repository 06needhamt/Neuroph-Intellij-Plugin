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
import com.intellij.util.PlatformIcons
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_TEST_LOCATION_KEY
import com.thomas.needham.neurophidea.Constants.COMMA_DELIMITED
import com.thomas.needham.neurophidea.actions.OpenExistingNetworkAction
import com.thomas.needham.neurophidea.actions.ShowTestNetworkFormAction
import com.thomas.needham.neurophidea.actions.ShowTrainNetworkFormAction
import com.thomas.needham.neurophidea.datastructures.NetworkConfiguration
import org.neuroph.core.NeuralNetwork
import org.neuroph.core.learning.SupervisedTrainingElement
import org.neuroph.core.learning.TrainingSet
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException
import java.io.ObjectInputStream
import java.util.*

/**
 * Created by Thomas Needham on 05/06/2016.
 */
class NetworkTester {
    var network : NeuralNetwork? = null
    var testingSet : TrainingSet<SupervisedTrainingElement?>? = null
    var testingData : DoubleArray? = null
    var properties = PropertiesComponent.getInstance()
    var inputSize : Int = 0
    var outputSize : Int = 0

    companion object Data{
        var networkPath = ""
        var testingSetPath = ""
        var outputPath = ""
    }

    constructor(path: String, testingSet: String, output: String){
        networkPath = path
        outputPath = output
        testingSetPath = testingSet
        network = LoadNetwork()
        this.testingSet = LoadTestingSet()

    }
    constructor(path : String, testingData : DoubleArray, output: String){
        networkPath = path
        outputPath = output
        this.testingData = testingData
        network = LoadNetwork()
        this.testingSet = CreateTestingSet()
    }

    private fun LoadNetwork() : NeuralNetwork?{
        val temp : NeuralNetwork?
        try {
            val file = File(networkPath)
            val fis = FileInputStream(file)
            val ois = ObjectInputStream(fis)
            temp = ois.readObject() as NeuralNetwork?
            inputSize = temp?.layers?.first()?.neuronsCount!! - 1
            outputSize = temp?.layers?.last()?.neuronsCount!!
            return temp
        }
        catch (ioe: IOException){
            ioe.printStackTrace(System.err)
            Messages.showErrorDialog(OpenExistingNetworkAction.project,"Error Reading Network From file", "Error")
            return null
        }
        catch (fnfe: FileNotFoundException){
            fnfe.printStackTrace(System.err)
            Messages.showErrorDialog(OpenExistingNetworkAction.project, "No Network Found at: ${ShowTestNetworkFormAction.properties.getValue(NETWORK_TO_TEST_LOCATION_KEY, "")}", "Error")
            return null
        }
    }
    private fun CreateTestingSet() : TrainingSet<SupervisedTrainingElement?>?{
        if(testingData == null)
            return null
        try {
            var set = TrainingSet<SupervisedTrainingElement?>(NetworkTrainer.networkConfiguration?.networkLayers?.first()!!, NetworkTrainer.networkConfiguration?.networkLayers?.last()!!)
            for (i in 0..testingData?.size!! step inputSize + outputSize) {
                val inputSize = NetworkTrainer.networkConfiguration?.networkLayers?.first()!!
                val outputSize = NetworkTrainer.networkConfiguration?.networkLayers?.last()!!
                set.addElement(SupervisedTrainingElement(testingData!!.copyOfRange(i, (i + inputSize)),
                        testingData!!.copyOfRange((i + inputSize), (i + inputSize + outputSize))))
            }
            return set
        }
        catch (ioobe: IndexOutOfBoundsException){
            ioobe.printStackTrace(System.err)
            Messages.showErrorDialog(ShowTrainNetworkFormAction.project, "Testing data does not contain the correct amount of inputs or outputs","Error")
            return null
        }
    }
    private fun LoadTestingSet() : TrainingSet<SupervisedTrainingElement?>?{
        return TrainingSet.createFromFile(testingSetPath,inputSize,outputSize, COMMA_DELIMITED) as TrainingSet<SupervisedTrainingElement?>?
    }

    fun TestNetwork() {
        var averageDevience : Double = 0.0
        var total : Double = 0.0
        var resultString : String = ""
        var file : File = File((outputPath + "/" + "Results ${Date().toString().replace(':','-')}.result").replace(' ', '_'))
        if(!file.exists())
            file.createNewFile()
        try{
            val fileWriter : FileWriter = FileWriter(file,false)
            val bufferedWriter : BufferedWriter = BufferedWriter(fileWriter)
            for(i in 0..testingSet?.elements()?.size!! - 1 step 1){
                var calculated : Double
                var expected : Double
                network?.setInput(*testingSet?.elementAt(i)?.input!!)
                network?.calculate()
                calculated = network?.output!![0]
                expected = testingSet?.elementAt(i)?.idealArray!![0]
                bufferedWriter.write("Caculated Output: " + calculated + "\n"); // print the calculated outputs
                bufferedWriter.write("Expected Output: " + expected + "\n"); // print the expected outputs
                bufferedWriter.write("Devience: " + (calculated - expected) + "\n");
                averageDevience += Math.abs(Math.abs(calculated) - Math.abs(expected)); //calculate the average deviance
                for(j in 0..network?.output?.size!! - 1 step 1){
                    total += network?.output!![j]
                }
                for(cols in 0..testingSet?.elementAt(i)?.inputArray?.size!! - 1 step 1){
                    resultString += "${testingSet?.elementAt(i)?.inputArray!![cols]}" + ", "; // append the inputs to the result
                }
                for(t in 0..network?.output?.size!! - 1 step 1){
                    resultString += "${network?.output!![t]}" + ", "; // append the outputs to the result
                    resultString = resultString.substring(0, resultString.length - 2); // Chop off final ", "
                    resultString += "\n";
                    bufferedWriter.write(resultString);
                    bufferedWriter.flush(); // write and flush the result to the file
                }
            }
            var message = "Average: ${total / testingSet?.elements()?.size!!}" + "\n" +
                            "Average Deviance: ${(averageDevience / testingSet?.elements()?.size!!) * 100}" + "%"
            Messages.showOkCancelDialog(ShowTestNetworkFormAction.project,message,"Results", PlatformIcons.CHECK_ICON)
            Messages.showOkCancelDialog(ShowTestNetworkFormAction.project,"Results Written to file ${file.path}","Success",PlatformIcons.CHECK_ICON)
        }
        catch(ioe: IOException){
            ioe.printStackTrace(System.err)
            Messages.showErrorDialog(ShowTestNetworkFormAction.project,"Error writing results to file","Error")
        }
    }
}