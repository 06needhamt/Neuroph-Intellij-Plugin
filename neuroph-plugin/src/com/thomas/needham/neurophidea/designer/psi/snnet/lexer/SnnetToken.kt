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
package com.thomas.needham.neurophidea.designer.psi.snnet.lexer

/**
 * Created by thoma on 24/06/2016.
 */
class SnnetToken {
    var value : String = ""
    var type : SnnetTokenType.Types = SnnetTokenType.Types.NEW_LINE
    var id : Int = Int.MIN_VALUE
    constructor(value: String){
        this.value = value
        this.id = ResolveID()
        this.type = ResolveType()
    }

    fun ResolveType() : SnnetTokenType.Types {
        when(value){
            ":" -> return SnnetTokenType.Types.COLON
            "," -> return SnnetTokenType.Types.COMMA
            ";" -> return SnnetTokenType.Types.SEMI_COLON
            "\n", "\r" -> return SnnetTokenType.Types.NEW_LINE
            else -> {
                try{
                    var value : Double = value.toDouble()
                }
                catch(nfe: NumberFormatException){
                    if((value.startsWith("\"") && value.endsWith("\""))){
                        return SnnetTokenType.Types.STRING_LITERAL
                    }
                    else{
                        return SnnetTokenType.Types.PROPERTY
                    }
                }
                return SnnetTokenType.Types.NUMERIC_LITERAL
            }
        }
    }

    fun ResolveID() : Int{
        when (type){
            SnnetTokenType.Types.COLON ->  {return 1.shl(SnnetTokenType.Types.COLON.ordinal)}
            SnnetTokenType.Types.COMMA -> {return 1.shl(SnnetTokenType.Types.COMMA.ordinal)}
            SnnetTokenType.Types.STRING_LITERAL -> {return 1.shl(SnnetTokenType.Types.STRING_LITERAL.ordinal)}
            SnnetTokenType.Types.NUMERIC_LITERAL -> {return 1.shl(SnnetTokenType.Types.NUMERIC_LITERAL.ordinal)}
            SnnetTokenType.Types.PROPERTY -> {return 1.shl(SnnetTokenType.Types.PROPERTY.ordinal)}
            SnnetTokenType.Types.SEMI_COLON -> {return 1.shl(SnnetTokenType.Types.SEMI_COLON.ordinal)}
            SnnetTokenType.Types.NEW_LINE -> {return 1.shl(SnnetTokenType.Types.NEW_LINE.ordinal)}
            else -> {return Int.MIN_VALUE}
        }
    }
}