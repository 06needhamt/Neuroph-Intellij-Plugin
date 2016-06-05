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

import org.jetbrains.annotations.NotNull
import org.neuroph.core.NeuralNetwork
import org.neuroph.core.learning.SupervisedTrainingElement
import org.neuroph.core.learning.TrainingSet
import java.io.Serializable

/**
 * Created by thoma on 05/06/2016.
 */
class NetworkResult : Serializable {
    var calculatedOutput : Array<Double>
    var expectedOutput : Array<Double>
    var deviance : Double

    companion object Output{
        var network : NeuralNetwork? = null
        var data : SupervisedTrainingElement? = null
        var networkInput : Array<Double> = arrayOf(0.0)
        var networkOutput : Array<Double> = arrayOf(0.0)
    }

    constructor(@NotNull net: NeuralNetwork?, @NotNull dat: SupervisedTrainingElement?, calculatedOutput: Array<Double>, expectedOutput: Array<Double>, deviance: Double){
        network = net
        data = dat
        networkInput = dat?.input!!.toTypedArray()
        networkOutput = dat?.desiredOutput!!.toTypedArray()
        this.calculatedOutput = calculatedOutput
        this.expectedOutput = expectedOutput
        this.deviance = deviance
    }
}