package com.thomas.needham.neurophidea.designer.psi.snnet.parser

import com.thomas.needham.neurophidea.designer.psi.snnet.lexer.SnnetToken
import com.thomas.needham.neurophidea.designer.psi.snnet.lexer.SnnetTokenType
import java.util.*

/**
 * Created by thoma on 29/06/2016.
 */
class SnnetParser {
    val tokensList : MutableList<SnnetToken>

    constructor(tokensList: LinkedList<SnnetToken>){
        this.tokensList = tokensList
    }

    fun Start() : Boolean{
        if(ResolveTokens())
            return false
        return true
    }

    private fun  ResolveTokens() : Boolean {
        var i = 0
        while(i < tokensList.size - 1){
            if(tokensList[i].type == SnnetTokenType.Types.PROPERTY && tokensList[i + 1].type == SnnetTokenType.Types.COLON){
                //val prop = SnnetProperties.
            }
        }
        return true
    }
}