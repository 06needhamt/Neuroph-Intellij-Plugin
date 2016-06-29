package com.thomas.needham.neurophidea.designer.psi.snnet.parser

import com.thomas.needham.neurophidea.Tuple3
import java.util.*

/**
 * Created by thoma on 29/06/2016.
 */
class SnnetProperties {
    val properties : MutableList<Tuple3<Class<*>, String, *>>?
    private var obj : SnnetProperties? = null
    private constructor(){
        properties = ArrayList<Tuple3<Class<*>, String, *>>()
        properties.add(Tuple3<Class<*>, String, String>(String::class.java,"NetworkType",null) as Tuple3<Class<*>,String,*>)
    }
    fun getInstance() : SnnetProperties{
        if(obj == null)
            obj = SnnetProperties()
        return obj!!
    }
}