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
package com.thomas.needham.neurophidea.project

import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

/**
 * Created by Thomas Needham on 06/06/2016.
 */
class NeurophModule : ModuleType<NeurophModuleBuilder>("Neuroph Project") {
    companion object Instance{
        val INSTANCE : NeurophModule? = NeurophModule.INSTANCE
    }

    override fun createModuleBuilder() : NeurophModuleBuilder {
        val build = NeurophModuleBuilder()
        return build
    }

    override fun getName() : String {
        return "Neuroph Project"
    }

    override fun getDescription() : String {
        return "Module With Integrated Neuroph Libraries"
    }

    override fun getNodeIcon(p0 : Boolean) : Icon? {
        return IconLoader.getTransparentIcon(IconLoader.getIcon("/icon2.png"))
    }
}