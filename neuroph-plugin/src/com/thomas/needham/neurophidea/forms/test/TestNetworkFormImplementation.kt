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
@file:JvmName("TestNetworkForm\$Ext") // Do Some Kotlin Sorcery!
@file:JvmMultifileClass() // Do Some Kotlin Sorcery!
package com.thomas.needham.neurophidea.forms.test

import com.thomas.needham.neurophidea.forms.test.TestNetworkBrowseButtonActionListener
import com.thomas.needham.neurophidea.forms.train.TrainNetworkButtonActionListener

/**
 * Created by Thomas Needham on 04/06/2016.
 */

fun TestNetworkForm.AddOnClickListeners(){
    val browseNetworkListener : TestNetworkBrowseButtonActionListener? = TestNetworkBrowseButtonActionListener()
    browseNetworkListener?.formInstance = this
    btnBrowseNetwork?.addActionListener(browseNetworkListener)
    val browseTestingSetListener : TestingSetBrowseButtonActionListener? = TestingSetBrowseButtonActionListener()
    browseTestingSetListener?.formInstance = this
    btnBrowseTestingSet?.addActionListener(browseTestingSetListener)
    val resultsActionListener : NetworkResultsBrowseActionListener? = NetworkResultsBrowseActionListener()
    resultsActionListener?.formInstance = this
    btnBrowseOutput?.addActionListener(resultsActionListener)
    val trainActionListener : TestNetworkButtonActionListener? = TestNetworkButtonActionListener()
    trainActionListener?.formInstance = this
    btnTestNetwork.addActionListener(trainActionListener)
}