/*
The MIT License (MIT)

Copyright (c) 2016 Tom Needham

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.thomas.needham.neurophidea.designer.psi.snnet.compiler.parser

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