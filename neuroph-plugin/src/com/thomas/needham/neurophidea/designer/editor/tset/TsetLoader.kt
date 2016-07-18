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
package com.thomas.needham.neurophidea.designer.editor.tset

import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.thomas.needham.neurophidea.actions.InitialisationAction
import org.neuroph.core.learning.TrainingSet
import org.neuroph.core.learning.TrainingElement
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.ObjectInputStream

/**
 * Created by thoma on 12/07/2016.
 */
class TsetLoader {
    val file : VirtualFile?
    constructor(file: VirtualFile?){
        this.file = file
    }

    fun LoadDataSet() : TrainingSet<TrainingElement?>? {
        try {
            val f = File(file?.path)
            val fis : FileInputStream? = f.inputStream()
            val ois : ObjectInputStream = ObjectInputStream(fis)
            return ois?.readObject() as TrainingSet<TrainingElement?>?
        } catch(ioe : IOException) {
            ioe.printStackTrace(System.err)
            Messages.showErrorDialog(InitialisationAction.project, "Error Loading Data Set From File", "Error")
        } catch(fnfe : FileNotFoundException) {
            fnfe.printStackTrace(System.err)
            Messages.showErrorDialog(InitialisationAction.project, "No Data Set found in file: ${file?.path}", "Error")
        }
        return null
    }
}