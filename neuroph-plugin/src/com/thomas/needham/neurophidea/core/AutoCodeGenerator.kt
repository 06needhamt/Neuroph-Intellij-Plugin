/*
    The MIT License (MIT)
    
    neuroph-plugin Copyright (c) 2016 thoma
    
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

package com.thomas.needham.neurophidea.core

import com.intellij.ide.DataManager
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.DataConstants
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.thomas.needham.neurophidea.Constants
import com.thomas.needham.neurophidea.actions.InitialisationAction
import com.thomas.needham.neurophidea.datastructures.NetworkConfiguration
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException
import java.io.ObjectInputStream
import java.util.*
import com.thomas.needham.neurophidea.Constants.GENERATE_CODE_KEY
import com.thomas.needham.neurophidea.Constants.GENERATE_JAVA_KEY
import com.thomas.needham.neurophidea.Constants.GENERATE_GROOVY_KEY
import com.thomas.needham.neurophidea.Constants.GENERATE_SCALA_KEY
import com.thomas.needham.neurophidea.Constants.GENERATE_KOTLIN_KEY
import com.thomas.needham.neurophidea.Tuple2
import com.thomas.needham.neurophidea.actions.ShowExportNetworkFormAction
import com.thomas.needham.neurophidea.forms.export.ExportSourceButtonActionListener

/**
 * Created by thoma on 26/09/2016.
 */
class AutoCodeGenerator {
    var java : JavaNetworkCodeGenerator? = null
    var scala : ScalaNetworkCodeGenerator? = null
    var groovy : GroovyNetworkCodeGenerator? = null
    var kotlin : KotlinNetworkCodeGenerator? = null
    val configs : ArrayList<Tuple2<NetworkConfiguration?, String>?>

    constructor(){
        project = InitialisationAction.project
        projectDirectory = InitialisationAction.project?.basePath
        isOpen = InitialisationAction.project?.isOpen
        if(project == null) {
            project = GetProject()
            projectDirectory = project?.basePath
            isOpen = project?.isOpen
        }
        this.configs = GetConfigs(project?.baseDir?.children!!)
    }

    private fun GetProject() : Project {
        val dataContext = DataManager.getInstance().getDataContext()
        val project = dataContext.getData(DataConstants.PROJECT) as Project
        return project
    }

    companion object Data{
        var project : Project? = null
        var projectDirectory : String? = ""
        var isOpen : Boolean? = false
    }

    fun GetConfigs(projectDir: Array<out VirtualFile?>) : ArrayList<Tuple2<NetworkConfiguration?, String>?>{
        val result : ArrayList<Tuple2<NetworkConfiguration?, String>?> = ArrayList()
        for(file: VirtualFile? in projectDir){
            if(file?.isDirectory!!){
                result += GetConfigs(file?.children!!)
            }
            else if(file?.extension?.equals("conf")!!){
                result.add(GetNetworkConfiguration(file!!))
            }
        }
        return result
    }

    private fun  GetNetworkConfiguration(file : VirtualFile) : Tuple2<NetworkConfiguration?, String>? {
        try{
            val f : File = File(file.path)
            val fis : FileInputStream = FileInputStream(f)
            val ois : ObjectInputStream = ObjectInputStream(fis)

            val obj = Tuple2<NetworkConfiguration?, String>(ois.readObject() as NetworkConfiguration?, file.path)
            ois.close()
            fis.close()
            return obj
        }
        catch(ioe: IOException){
            Messages.showErrorDialog(project, "Error Loading Network Configuration from File: ${file.path}", "ERROR")
            return null
        }
        catch(fnfe: FileNotFoundException){
            Messages.showErrorDialog(project, "File Not Found: ${file.path}", "ERROR")
            return null
        }
    }

    fun GenerateCode() : Boolean{
        for(conf: Tuple2<NetworkConfiguration?, String>? in configs){
            if(conf != null){
                val settings = GetGenerationSettings()
                val path : String = conf.valueY?.substring(0 .. conf.valueY?.lastIndexOf('.')!!)!!
                if(settings[0]){ // Java
                    val java = JavaNetworkCodeGenerator(conf.valueX, path + "java")
                    val source = java.GenerateCode()
                    WriteCode(source, path + "java")
                }
                if(settings[1]){ // Groovy
                    val groovy = GroovyNetworkCodeGenerator(conf.valueX, path + "groovy")
                    val source = groovy.GenerateCode()
                    WriteCode(source, path + "groovy")
                }
                if(settings[2]){ // Scala
                    val scala = ScalaNetworkCodeGenerator(conf.valueX, path + "scala")
                    val source = scala.GenerateCode()
                    WriteCode(source, path + "scala")
                }
                if(settings[3]){ // Kotlin
                    val kotlin = KotlinNetworkCodeGenerator(conf.valueX, path + "kt")
                    val source = kotlin.GenerateCode()
                    WriteCode(source, path + "kt")
                }
            }
        }
        return true
    }

    private fun GetGenerationSettings() : Array<Boolean> {
        val settings = Array<Boolean>(4,{ e -> false })
        val properties : PropertiesComponent = PropertiesComponent.getInstance()
        var shouldGenerate : Boolean = false
        shouldGenerate = properties.getBoolean(GENERATE_CODE_KEY, false)
        if(!shouldGenerate)
            return Array<Boolean>(4,{ e -> false })
        settings[0] = properties.getBoolean(GENERATE_JAVA_KEY, false)
        settings[1] = properties.getBoolean(GENERATE_GROOVY_KEY, false)
        settings[2] = properties.getBoolean(GENERATE_SCALA_KEY, false)
        settings[3] = properties.getBoolean(GENERATE_KOTLIN_KEY, false)
        return settings
    }

    fun WriteCode(source: String, path: String) : Unit {
        try {
            val file = File(path)
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
            Messages.showErrorDialog(project,"Error occurred while writing source code to file", "Error")
        }
        catch(fnfe: FileNotFoundException){
            fnfe.printStackTrace(System.err)
            Messages.showErrorDialog(project,"File ${path} does not exist", "Error")
        }
    }
}
