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
@file:JvmName("CreateNetworkForm\$Ext") // Do Some Kotlin Sorcery!
@file:JvmMultifileClass() // Do Some Kotlin Sorcery!
package com.thomas.needham.neurophidea.forms.create

import com.thomas.needham.neurophidea.datastructures.LearningRules
import com.thomas.needham.neurophidea.datastructures.NetworkTypes
import com.thomas.needham.neurophidea.datastructures.TransferFunctions
import com.thomas.needham.neurophidea.forms.create.CreateNetworkForm

/**
 * Created by thoma on 26/05/2016.
 */

fun com.thomas.needham.neurophidea.forms.create.CreateNetworkForm.PopulateTransferFunctions() {
    for (i in 0..com.thomas.needham.neurophidea.datastructures.TransferFunctions.Functions.values().size - 1) {
        cmbTransferFunction.addItem(com.thomas.needham.neurophidea.datastructures.TransferFunctions.Names.friendlyNames[i])
    }
}

fun com.thomas.needham.neurophidea.forms.create.CreateNetworkForm.PopulateNetworktypes() {
    for (i in 0..com.thomas.needham.neurophidea.datastructures.NetworkTypes.Types.values().size - 1) {
        cmbNetworkType.addItem(com.thomas.needham.neurophidea.datastructures.NetworkTypes.Names.friendlyNames[i])
    }
}

fun com.thomas.needham.neurophidea.forms.create.CreateNetworkForm.PopulateLearningRules() {
    for (i in 0..com.thomas.needham.neurophidea.datastructures.LearningRules.Rules.values().size - 1) {
        cmbLearningRule.addItem(com.thomas.needham.neurophidea.datastructures.LearningRules.Names.friendlyNames[i])
    }
}
fun com.thomas.needham.neurophidea.forms.create.CreateNetworkForm.AddOnClickListeners() {
    val trainingListener : com.thomas.needham.neurophidea.forms.create.TrainingSetBrowseButtonActionListener? = com.thomas.needham.neurophidea.forms.create.TrainingSetBrowseButtonActionListener()
    trainingListener?.formInstance = this
    btnBrowseTrainingData.addActionListener(trainingListener)
    val testingListener : com.thomas.needham.neurophidea.forms.create.TestingSetBrowseButtonActionListener? = com.thomas.needham.neurophidea.forms.create.TestingSetBrowseButtonActionListener()
    testingListener?.formInstance = this
    btnBrowseTestingData.addActionListener(testingListener)
    val outputListener : com.thomas.needham.neurophidea.forms.create.NetworkOutputBrowseButtonActionListener? = com.thomas.needham.neurophidea.forms.create.NetworkOutputBrowseButtonActionListener()
    outputListener?.formInstance = this
    btnBrowseOutput.addActionListener(outputListener)
    val saveNetworkListener : com.thomas.needham.neurophidea.forms.create.SaveNetworkButtonActionListener? = com.thomas.needham.neurophidea.forms.create.SaveNetworkButtonActionListener()
    saveNetworkListener?.formInstance = this
    btnSaveNetwork.addActionListener(saveNetworkListener)
}