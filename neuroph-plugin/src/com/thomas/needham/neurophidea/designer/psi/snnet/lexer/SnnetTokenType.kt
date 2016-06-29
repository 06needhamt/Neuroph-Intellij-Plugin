package com.thomas.needham.neurophidea.designer.psi.snnet.lexer

/**
 * Created by thoma on 24/06/2016.
 */
class SnnetTokenType {
    enum class Types(value: Int){
        PROPERTY(0b00000000),
        COLON(0b00000001),
        COMMA(0b00000010),
        SEMI_COLON(0b00000100),
        STRING_LITERAL(0b00001000),
        NUMERIC_LITERAL(0b00010000),
        NEW_LINE(0b00100000);
    }
}