package com.thomas.needham.neurophidea.exceptions

/**
 * Created by thoma on 28/05/2016.
 */
class InvalidLayerSizeException(message : String?) : Exception(message) {
    override val message : String?
        get() =  if(super.message == "") "Layer Sizes must be greater than 0" else super.message
}