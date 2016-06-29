package com.thomas.needham.neurophidea

/**
 * Created by thoma on 29/06/2016.
 */
open class Tuple3<X, Y, Z> {
    var valueX : X? = null
    var valueY : Y? = null
    var valueZ : Z? = null
    constructor(x: X?, y: Y?, z: Z?){
        this.valueX = x
        this.valueY = y
        this.valueZ = z
    }
    companion object Blank{
        @JvmStatic val BLANK = Tuple3<Any?,Any?,Any?>(null,null,null)
    }
}