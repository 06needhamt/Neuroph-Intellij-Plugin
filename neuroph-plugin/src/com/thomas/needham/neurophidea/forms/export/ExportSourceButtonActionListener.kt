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
package com.thomas.needham.neurophidea.forms.export

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ui.Messages
import com.intellij.util.PlatformIcons
import com.thomas.needham.neurophidea.core.JavaNetworkCodeGenerator
import com.thomas.needham.neurophidea.datastructures.NetworkConfiguration
import com.thomas.needham.neurophidea.Constants.NETWORK_TO_EXPORT_LOCATION_KEY
import com.thomas.needham.neurophidea.Constants.SOURCE_TO_EXPORT_LOCATION_KEY
import com.thomas.needham.neurophidea.actions.ShowExportNetworkFormAction
import com.thomas.needham.neurophidea.core.GroovyNetworkCodeGenerator
import com.thomas.needham.neurophidea.core.ICodeGenerator
import com.thomas.needham.neurophidea.core.KotlinNetworkCodeGenerator
import com.thomas.needham.neurophidea.core.ScalaNetworkCodeGenerator
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.ObjectInputStream

/**
 * Created by Thomas Needham on 29/05/2016.
 */
class ExportSourceButtonActionListener : ActionListener {
    var formInstance : ExportNetworkForm? = null
    var network : NetworkConfiguration? = null
    var codeGenerator : ICodeGenerator? = null
    companion object Data{
        val properties = PropertiesComponent.getInstance()
        var inputPath = ""
        var outputPath = ""
        val defaultPath = ""
        val allowedFileTypes = arrayOf("java", "groovy")
        val getNetwork : (String) -> NetworkConfiguration? = { path ->
            try {
                val file = File(path)
                val fis = FileInputStream(file)
                val ois = ObjectInputStream(fis)
                ois.readObject() as NetworkConfiguration?
            }
            catch(ioe: IOException){
                ioe.printStackTrace(System.err)
                Messages.showErrorDialog(ShowExportNetworkFormAction.project,"Error occurred while reading network from file", "Error")
                null
            }
            catch(fnfe: FileNotFoundException){
                fnfe.printStackTrace(System.err)
                Messages.showErrorDialog(ShowExportNetworkFormAction.project,"File ${path} does not exist", "Error")
                null
            }
        }
        var sourceCode = ""
        val writeCode : (String) -> Unit = { source ->
            try {
                val file = File(outputPath)
                if(!file.exists())
                    file.createNewFile()
                val fw = FileWriter(file, false)
                val bw = BufferedWriter(fw)
                bw.write(source)
                bw.flush()
                bw.close()
            }
            catch(ioe: IOException){
                ioe.printStackTrace(System.err)
                Messages.showErrorDialog(ShowExportNetworkFormAction.project,"Error occurred while writing source code to file", "Error")
            }
            catch(fnfe: FileNotFoundException){
                fnfe.printStackTrace(System.err)
                Messages.showErrorDialog(ShowExportNetworkFormAction.project,"File ${source} does not exist", "Error")
            }
        }
    }
    override fun actionPerformed(e : ActionEvent?) {
        val language = formInstance?.cmbLanguage?.selectedItem as String
        inputPath = properties.getValue(NETWORK_TO_EXPORT_LOCATION_KEY,defaultPath)
        network = getNetwork(inputPath)
        when(language){
            "Java" -> {
                outputPath = properties.getValue(SOURCE_TO_EXPORT_LOCATION_KEY,defaultPath) + "/" + "${network?.networkName}.java"
                codeGenerator = JavaNetworkCodeGenerator(network, outputPath)
                if(codeGenerator == null) return
                sourceCode = (codeGenerator as JavaNetworkCodeGenerator).GenerateCode()
            }
            "Groovy" -> {
                outputPath = properties.getValue(SOURCE_TO_EXPORT_LOCATION_KEY,defaultPath) + "/" + "${network?.networkName}.groovy"
                codeGenerator = GroovyNetworkCodeGenerator(network, outputPath)
                if(codeGenerator == null) return
                sourceCode = (codeGenerator as GroovyNetworkCodeGenerator).GenerateCode()
            }
            "Kotlin" -> {
                outputPath = properties.getValue(SOURCE_TO_EXPORT_LOCATION_KEY,defaultPath) + "/" + "${network?.networkName}.kt"
                codeGenerator = KotlinNetworkCodeGenerator(network, outputPath)
                if(codeGenerator == null) return
                sourceCode = (codeGenerator as KotlinNetworkCodeGenerator).GenerateCode()
            }
            "Scala" -> {
                outputPath = properties.getValue(SOURCE_TO_EXPORT_LOCATION_KEY, defaultPath) + "/" + "${network?.networkName}.scala"
                codeGenerator = ScalaNetworkCodeGenerator(network, outputPath)
                if(codeGenerator == null) return
                sourceCode = (codeGenerator as ScalaNetworkCodeGenerator).GenerateCode()
            }
            else -> {
                throw UnsupportedOperationException("Invalid Language Selected: ${language}")
            }
        }
        writeCode(sourceCode)
        Messages.showOkCancelDialog(ShowExportNetworkFormAction.project,"Source code successfully written to file", "Success", PlatformIcons.COPY_ICON)
    }
}