package com.thomas.needham.neurophidea.exceptions

/**
 * Created by Thomas Needham on 31/05/2016.
 */
class UnknownTransferFunctionException(message: String?) : Exception(message) {
    override val message : String?
        get() =  if(super.message == "") "Unknown Transfer Function" else super.message
}