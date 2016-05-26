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
@file:JvmName("CreateNetworkForm\$Ext")
@file:JvmMultifileClass()
package com.thomas.needham.neurophidea.forms

import com.thomas.needham.neurophidea.LearningRules
import com.thomas.needham.neurophidea.NetworkTypes
import com.thomas.needham.neurophidea.TransferFunctions

/**
 * Created by thoma on 26/05/2016.
 */

fun CreateNetworkForm.PopulateTransferFunctions() {
    for (i in 0..TransferFunctions.Functions.values().size - 1) {
        cmbTransferFunction.addItem(TransferFunctions.friendlyNames[i])
    }
}

fun CreateNetworkForm.PopulateNetworktypes() {
    for (i in 0..NetworkTypes.Types.values().size - 1) {
        cmbNetworkType.addItem(NetworkTypes.friendlyNames[i])
    }
}

fun CreateNetworkForm.PopulateLearningRules() {
    for (i in 0..LearningRules.Rules.values().size - 1) {
        cmbLearningRule.addItem(LearningRules.friendlyNames[i])
    }
}
fun CreateNetworkForm.AddOnClickListeners() {
    val trainingListener : TrainingSetBrowseButtonActionListener? = TrainingSetBrowseButtonActionListener()
    trainingListener?.formInstance = this
    btnBrowseTrainingData.addActionListener(trainingListener)
}