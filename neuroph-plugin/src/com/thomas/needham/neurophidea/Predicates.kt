package com.thomas.needham.neurophidea

/**
 * Created by thoma on 28/05/2016.
 */
object Predicates {
    @JvmStatic val EqualTo : (Int,Int) -> Boolean = { a,b ->
        a == b
    }

    @JvmStatic val EqualToZero : (Int) -> Boolean = { e ->
        e == 0
    }

    @JvmStatic val EqualToOrLessThan : (Int,Int) -> Boolean = { a,b ->
        a <= b
    }
}