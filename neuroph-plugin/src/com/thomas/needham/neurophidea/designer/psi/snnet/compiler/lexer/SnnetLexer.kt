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
package com.thomas.needham.neurophidea.designer.psi.snnet.compiler.lexer

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.thomas.needham.neurophidea.actions.CompileSnnetFileAction
import com.thomas.needham.neurophidea.actions.InitialisationAction
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.util.*

/**
 * Created by thoma on 27/06/2016.
 */
class SnnetLexer {
    val tokenList : MutableList<SnnetToken>
    val tokenStringList : LinkedList<String>
    var inputString : String = ""
    companion object Data{
        var project : Project? = InitialisationAction.project
        var projectDirectory : String? = project?.basePath
        var isOpen : Boolean? = project?.isOpen
        var inputFile : VirtualFile? = null
    }
    private constructor(tokenList: LinkedList<SnnetToken>, tokenStringList: LinkedList<String>){
        this.tokenList = tokenList
        this.tokenStringList = tokenStringList
        inputFile = null
    }
    constructor(file: VirtualFile?){
        tokenList = LinkedList<SnnetToken>()
        tokenStringList = LinkedList<String>()
        inputFile = file
    }

    fun Start() : Boolean{
        if(!ReadInput())
            return false
        if(!TokensizeInput())
            return false
        if(!GenerateTokens())
            return false
        return true
    }

    private fun  ReadInput() : Boolean {
        try{
            val file = File(inputFile?.path)
            val fis = FileReader(file)
            val br = BufferedReader(fis)
            inputString = br.readText()
            br.close()
            return inputString != ""
        }
        catch (ioe: IOException){
            ioe.printStackTrace(System.err)
            Messages.showErrorDialog(CompileSnnetFileAction.project,"Error Loading Source From File: ${inputFile?.path}","Error")
            return false
        }
        catch(fnfe: FileNotFoundException){
            fnfe.printStackTrace(System.err)
            Messages.showErrorDialog(CompileSnnetFileAction.project,"No Source Code Found in File: ${inputFile?.path}","Error")
            return false
        }
    }

    private fun  TokensizeInput() : Boolean {
        val split = inputString.split(' ','\r','\n')
        for(str: String in split){
            tokenStringList.add(str)
        }
        return true
    }

    private fun GenerateTokens() : Boolean{
        var temp : String = ""
        var i = 0
        while(i < tokenStringList.size){
            if(tokenStringList[i].startsWith("\"") && !tokenStringList[i].endsWith("\"")){
                try {
                    while (!temp.endsWith("\"")) {
                        temp += tokenStringList[i]
                        if(!temp.endsWith("\""))
                            temp += " "
                        i++
                    }
                    tokenList.add(SnnetToken(temp))
                    temp = ""
                }
                catch(iobe: IndexOutOfBoundsException){
                    iobe.printStackTrace()
                    Messages.showErrorDialog(CompileSnnetFileAction.project,"COMPILE ERROR Unterminated String Literal","ERROR")
                    return false
                }
            }
            else {
                tokenList.add(SnnetToken(tokenStringList[i]))
                i++
            }
        }
        tokenList.removeAll { e ->
            e.value.equals("")
        }
        return true
    }

}