package com.thomas.needham.neurophidea.designer.psi.snnet.parser

import com.intellij.openapi.ui.Messages
import com.thomas.needham.neurophidea.Tuple3
import com.thomas.needham.neurophidea.actions.InitialisationAction
import java.util.*

/**
 * Created by thoma on 29/06/2016.
 */
class SnnetProperties<T : Any> {
    val properties : MutableList<Tuple3<Class<*>, String, T>>?
    private constructor(){
        properties = ArrayList<Tuple3<Class<*>, String, T>>()
        properties.add(Tuple3<Class<*>, String, T>(String::class.java, "NetworkName", null))
        properties.add(Tuple3<Class<*>, String, T>(String::class.java, "NetworkType", null))
        properties.add(Tuple3<Class<*>, String, T>(Array<Double>::class.java, "NetworkLayers", null))
        properties.add(Tuple3<Class<*>, String, T>(String::class.java, "NetworkLearningRule", null))
        properties.add(Tuple3<Class<*>, String, T>(String::class.java, "NetworkTransferFunction", null))
    }
    companion object Instance {
        private var obj : SnnetProperties<Any>? = null
        @JvmStatic fun getInstance() : SnnetProperties<Any> {
            if (obj == null)
                obj = SnnetProperties()
            return obj!!
        }
    }

    inline fun<reified V : T> setValue(name: String, value: V?) : Boolean{
        val prop = properties?.firstOrNull({ e ->
            e.valueY.equals(name)
        })
        if(prop == null){
            Messages.showErrorDialog(InitialisationAction.project, "COMPILE ERROR: No Property ${name} Found","ERROR")
            return false
        }
        if(V::class.java.simpleName != prop.valueX?.simpleName){
            Messages.showErrorDialog(InitialisationAction.project, "COMPILE ERROR: Incorrect value type for property ${name} " +
                    "Expected ${V::class.java.simpleName} Found ${prop.valueX?.simpleName}","ERROR")
            return false
        }
        val index = properties?.indexOf(prop)
        properties!![index!!].valueZ = value
        return true
    }
}