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
@file:JvmName("NetworkOutputForm\$Ext") // Do Some Kotlin Sorcery!
@file:JvmMultifileClass() // Do Some Kotlin Sorcery!
package com.thomas.needham.neurophidea.forms.output

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import javax.swing.ListModel

/**
 * Created by Thomas Needham on 09/06/2016.
 */

fun NetworkOutputForm.PopulateLists(path: String){
    val file : File = File(path)
    val fr : FileReader? = FileReader(file)
    val br : BufferedReader? = BufferedReader(fr)
    for(line : String in br?.lines()!!){
        if(line.startsWith("Caculated Output: ")){
            val temp : String?
            temp = line.replace("Caculated Output: ","",true)
            this.actualItems.addElement(temp.toDouble())
        }
        else if(line.startsWith("Expected Output: ")){
            val temp : String?
            temp = line.replace("Expected Output: ", "")
            this.expectedItems.addElement(temp.toDouble())
        }
        else if(line.startsWith("Devience: ")){
            val temp : String?
            temp = line.replace("Devience: ","")
            this.differenceItems.addElement(temp.toDouble())
        }
        else{
            continue
        }
    }
    br?.close()
    this.lstActual?.model = this.differenceItems as ListModel<Any>
    this.lstExpected.model = this.expectedItems as ListModel<Any>
    this.lstDifference.model = this.differenceItems as ListModel<Any>
}

fun NetworkOutputForm.AddOnClickListeners(){
    val browseListener : ResultsOutputBrowseButtonActionListener? = ResultsOutputBrowseButtonActionListener()
    browseListener?.formInstance = this
    this.btnBrowseSaveLocation.addActionListener(browseListener)
    val saveListener : SaveOutputButtonActionListener? = SaveOutputButtonActionListener()
    saveListener?.formInstance = this
    this.btnSaveOutput.addActionListener(saveListener)
}