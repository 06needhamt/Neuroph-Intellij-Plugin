package com.thomas.needham.neurophidea.designer.psi.snnet.lexer

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
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
            Messages.showErrorDialog(null,"Error Loading Source From File: ${inputFile?.path}","Error")
            return false
        }
        catch(fnfe: FileNotFoundException){
            fnfe.printStackTrace(System.err)
            Messages.showErrorDialog(null,"No Source Code Found in File: ${inputFile?.path}","Error")
            return false
        }
    }

    private fun  TokensizeInput() : Boolean {
        val builder : StringBuilder = StringBuilder()
        for(ch: Char in inputString){
            if(ch == ' ' || ch == '\n' || ch == ',' || ch == ';' || ch == ':'){
                tokenStringList.add(builder.toString())
                builder.setLength(0)
                builder.append(ch)
                tokenStringList.add(builder.toString())
                builder.setLength(0)
                continue
            }
            builder.append(ch)
        }
        return true
    }

    private fun GenerateTokens() : Boolean{
        var temp : String = ""
        for(i in 0..tokenStringList.size - 1 ){
            if(tokenStringList[i].startsWith("\"") && !tokenStringList[i].endsWith("\"")){
                while(!temp.endsWith("\"")){
                    temp += tokenStringList[i]
                    continue
                }
                tokenList.add(SnnetToken(temp))
                temp = ""
            }
            else {
                tokenList.add(SnnetToken(tokenStringList[i]))
            }
        }
        return true
    }

}