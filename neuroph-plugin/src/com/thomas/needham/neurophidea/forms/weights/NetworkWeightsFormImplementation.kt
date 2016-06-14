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
@file:JvmName("NetworkWeightsForm\$Ext") // Do Some Kotlin Sorcery!
@file:JvmMultifileClass()

// Do Some Kotlin Sorcery!
package com.thomas.needham.neurophidea.forms.weights

import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.thomas.needham.neurophidea.Utils
import org.neuroph.core.Layer
import org.neuroph.core.Neuron
import java.awt.GridLayout
import java.awt.Insets
import javax.swing.JButton
import javax.swing.JTextField

/**
 * Created by thoma on 13/06/2016.
 */

fun NetworkWeightsForm.GetNetworkWeights() : Array<DoubleArray> {
    val neuronCountFun : () -> Int = {
        var total : Int = 0
        for (layer : Layer? in this.network.layers) {
            total += layer?.neuronsCount!!
        }
        total
    }
    val populateWeights : () -> Array<DoubleArray> = {
        val output : Array<DoubleArray> = Utils.array2dOfDouble(this.network.layersCount, neuronCountFun())
        for (i in 0..this.network.layersCount - 1) {
            output[i] = DoubleArray(this.network.layers[i].neuronsCount)
        }
        for (i in 0..this.network.layersCount - 1) {
            for (j in 0..this.network.layers[i].neurons.size - 1) {
                if (network.layers[i].neurons[j].weightsVector.size == 0) {
                    output[i][j] = 0.0
                    continue
                }
                output[i][j] = network.layers[i].neurons[j].weightsVector[0].getValue()
            }
        }
        output
    }
    val weights = populateWeights()
    return weights
}

fun NetworkWeightsForm.CreateFields() : Array<Array<JTextField?>>? {
    val inherit : (Int) -> JTextField = { i ->
        JTextField()
    }
    var textFields : Array<Array<JTextField?>>? = null
    this.inner.layout = GridLayout(this.weights.size + 1, 0, 5, 5)
    for (i in 0..this.weights.size - 1) {
        textFields = Utils.array2d<JTextField?>(this.weights.size, this.weights[i].size, inherit)
        for (j in 0..this.weights[i].size - 1) {
            textFields[i][j] = JTextField(this.weights[i][j].toString())
            this.inner.add(textFields[i][j], GridConstraints(j, i, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))

        }
    }
    return textFields
}

fun NetworkWeightsForm.CreateSaveButton() : JButton? {
    val button = JButton("Save Network Weights")
    val listener : SaveNetworkWeightsButtonActionListener? = SaveNetworkWeightsButtonActionListener()
    val rows : Int = (this.inner.layout as GridLayout?)?.rows!!
    val cols : Int = (this.inner.layout as GridLayout?)?.columns!!
    listener?.formInstance = this
    button.addActionListener(listener)
    this.inner.add(button, GridConstraints(rows + 1, 0, rows, cols, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false))
    return button
}
