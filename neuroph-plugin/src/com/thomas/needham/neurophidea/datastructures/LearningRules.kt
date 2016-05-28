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
package com.thomas.needham.neurophidea.datastructures

import java.io.Serializable

/**
 * Created by thoma on 25/05/2016.
 */

class LearningRules : Serializable {
    enum class Rules(nameString: String) {
        //Enum must be nested within a class to avoid a NoClassDefFoundException possible Kotlin bug???
        BACK_PROPAGATION("Back Propagation"),
        DYNAMIC_BACK_PROPAGATION("Dynamic Back Propagation");
        //TODO Add more learning rules
    }
    companion object Names{
        @JvmStatic val classNames = arrayOf("BackPropagation", "DynamicBackPropagation")
        @JvmStatic val friendlyNames = arrayOf("Back Propagation", "Dynamic Back Propagation")
        @JvmStatic fun GetClassName(v: LearningRules.Rules) : String{
            return classNames[v.ordinal]
        }
        //TODO Add more learning rules
    }
}