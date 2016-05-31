package com.thomas.needham.neurophidea.exceptions

/**
 * Created by thoma on 30/05/2016.
 */
class UnknownNetworkTypeException(message: String?) : Exception(message){
    override val message : String?
        get() =  if(super.message == "") "Unknown Network Type" else super.message
}