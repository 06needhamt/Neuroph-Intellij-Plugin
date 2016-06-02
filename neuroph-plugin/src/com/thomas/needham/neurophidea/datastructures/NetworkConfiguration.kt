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
package com.thomas.needham.neurophidea.datastructures

import java.io.Serializable

/**
 * Created by thoma on 27/05/2016.
 */
class NetworkConfiguration : Serializable {
    var networkName : String
    var networkType : NetworkTypes.Types
    var networkLayers : Array<Int>
    var networkLearningRule : LearningRules.Rules
    var networkTransferFunction : TransferFunctions.Functions
    var networkTrainingDataPath : String
    var networkTestingDataPath : String
    var networkOutputPath : String

    companion object Examples{
        val SingleLayerPerceptron = NetworkConfiguration("SingleLayerPerceptron", NetworkTypes.Types.PERCEPTRON,
                arrayOf(2, 1), LearningRules.Rules.BACK_PROPAGATION, TransferFunctions.Functions.GAUSSIAN,
                "Train Data", "Test Data", "Output Path")
        val MultiLayerPerceptron = NetworkConfiguration("MultiLayerPerceptron", NetworkTypes.Types.PERCEPTRON,
                arrayOf(4, 2, 1), LearningRules.Rules.BACK_PROPAGATION, TransferFunctions.Functions.GAUSSIAN,
                "Train Data", "Test Data", "Output Path")
    }

    constructor(networkName : String, networkType : NetworkTypes.Types, networkLayers : Array<Int>,
                networkLearningRule : LearningRules.Rules, networkTransferFunction : TransferFunctions.Functions,
                networkTrainingDataPath : String, networkTestingDataPath: String, networkOutputPath : String) {
        this.networkName = networkName
        this.networkType = networkType
        this.networkLayers = networkLayers
        this.networkLearningRule = networkLearningRule
        this.networkTransferFunction = networkTransferFunction
        this.networkTrainingDataPath = networkTrainingDataPath
        this.networkTestingDataPath = networkTestingDataPath
        this.networkOutputPath = networkOutputPath
    }

    override fun toString() : String {
        val getLayersString : (Array<Int>) -> String = { layers -> // Do Some Kotlin Sorcery!
            var result = ""
            for(i : Int in layers) {
                result += i.toString()
            }
            result
        }
        return "Name: " + networkName + "\n" +
                "Type: " + networkType.name + "\n" +
                "Layers: " + getLayersString(networkLayers) + "\n" +
                "Learning Rule: " + networkLearningRule.name + "\n" +
                "Transfer Function: " + networkTransferFunction.name + "\n"
    }

}