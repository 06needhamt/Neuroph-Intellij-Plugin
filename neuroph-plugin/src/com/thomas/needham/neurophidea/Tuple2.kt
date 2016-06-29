package com.thomas.needham.neurophidea

/**
 * Created by thoma on 29/06/2016.
 */
open class Tuple2<X, Y> {
    var valueX : X? = null
    var valueY : Y? = null
    constructor(x: X?, y: Y?){
        this.valueX = x
        this.valueY = y
    }
    companion object Blank{
        @JvmStatic val BLANK = Tuple2<Any?,Any?>(null,null)
    }
}