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
package com.thomas.needham.neurophidea.designer.psi.snnet.compiler.parser

import com.intellij.openapi.ui.Messages
import com.thomas.needham.neurophidea.actions.CompileSnnetFileAction
import com.thomas.needham.neurophidea.designer.psi.snnet.compiler.lexer.SnnetToken
import com.thomas.needham.neurophidea.designer.psi.snnet.compiler.lexer.SnnetTokenType
import java.util.*

/**
 * Created by thoma on 29/06/2016.
 */
class SnnetParser {
    val tokensList : MutableList<SnnetToken>
    val properties : SnnetProperties<Any>

    constructor(tokensList: MutableList<SnnetToken>){
        this.tokensList = tokensList
        properties = SnnetProperties.getInstance()
    }

    fun Start() : Boolean{
        if(!ResolveStatements())
            return false
        return true
    }

    private fun ResolveStatements() : Boolean {
        var i = 0
        while(i < tokensList.size - 1){
            try {
                if(tokensList[i].value.endsWith(";")){
                    i++
                }
                else if (tokensList[i].type == SnnetTokenType.Types.PROPERTY && tokensList[i + 1].type == SnnetTokenType.Types.COLON) {
                    if(tokensList[i + 2].value.endsWith(",")) {
                        val name = tokensList[i].value
                        val strings = ArrayList<String>()
                        val doubles = ArrayList<Double>()
                        var end = false
                        i += 2

                        while (!end || tokensList[i].value.endsWith(",") ) {
                            if(tokensList[i].value.endsWith(";"))
                                end = true
                            tokensList[i] = SnnetToken(tokensList[i].value.substring(0, tokensList[i].value.length - 1))
                            if (tokensList[i].type == SnnetTokenType.Types.STRING_LITERAL) {
                                strings.add(tokensList[i].value)
                                i++
                            } else if (tokensList[i].type == SnnetTokenType.Types.NUMERIC_LITERAL) {
                                try {
                                    doubles.add(tokensList[i].value.toDouble())
                                    i++
                                } catch(nfe : NumberFormatException) {
                                    nfe.printStackTrace()
                                    Messages.showErrorDialog(CompileSnnetFileAction.project, "COMPILE ERROR Expected numeric literal found: ${tokensList[i].value}", "ERROR")
                                    return false
                                }
                            }
                            else{ // ERROR
                                Messages.showErrorDialog(CompileSnnetFileAction.project, "COMPILE ERROR Found Unexpected Token ${tokensList[i].value}","Error")
                                return false
                            }
                            if(i >= tokensList.size)
                                break
                        }
                        properties.setValue<Array<Double>>(name,doubles.toTypedArray())
                    }
                    else {
                        if (!properties.setValue<String>(tokensList[i].value, tokensList[i + 2].value.replace("\"", "")))
                            return false
                        i += 3
                    }
                }
                else{ // ERROR
                    Messages.showErrorDialog(CompileSnnetFileAction.project, "COMPILE ERROR Found Unexpected Token ${tokensList[i].value}","Error")

                }
            }
            catch(iobe: IndexOutOfBoundsException){
                iobe.printStackTrace(System.err)
                Messages.showErrorDialog(CompileSnnetFileAction.project,"COMPILE ERROR Unterminated Statement", "Error")
                return false
            }
        }
        return true
    }
}