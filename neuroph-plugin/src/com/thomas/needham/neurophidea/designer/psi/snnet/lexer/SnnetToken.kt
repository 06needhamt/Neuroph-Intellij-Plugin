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