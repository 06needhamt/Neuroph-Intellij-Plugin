package com.thomas.needham.neurophidea.designer.psi.snnet.parser

import com.intellij.openapi.ui.Messages
import com.thomas.needham.neurophidea.Tuple3
import com.thomas.needham.neurophidea.actions.InitialisationAction
import java.util.*

/**
 * Created by thoma on 29/06/2016.
 */
class SnnetProperties {
    val properties : MutableList<Tuple3<Class<*>, String, *>>?
    private constructor(){
        properties = ArrayList<Tuple3<Class<*>, String, *>>()
        properties.add(Tuple3<Class<*>, String, String>(String::class.java,"NetworkType",null) as Tuple3<Class<*>, String, *>)
    }
    companion object Instance {
        private var obj : SnnetProperties? = null
        @JvmStatic fun getInstance() : SnnetProperties {
            if (obj == null)
                obj = SnnetProperties()
            return obj!!
        }
    }

    inline fun<reified V : Any?> setValue(name: String, value: V?){
        val prop = properties?.firstOrNull({ e ->
            e.valueY.equals(name)
        })
        if(prop == null){
            Messages.showErrorDialog(InitialisationAction.project, "COMPILE ERROR: No Property ${name} Found","ERROR")
            return
        }
        if(V::class.javaClass != prop.valueX){
            Messages.showErrorDialog(InitialisationAction.project, "COMPILE ERROR: Incorrect value type for property ${name} " +
                    "Expected ${V::class.javaClass.name} Found ${prop.valueX?.name}","ERROR")
            return
        }
    }
}