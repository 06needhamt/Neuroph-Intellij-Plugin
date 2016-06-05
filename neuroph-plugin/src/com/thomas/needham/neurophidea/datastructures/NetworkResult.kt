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