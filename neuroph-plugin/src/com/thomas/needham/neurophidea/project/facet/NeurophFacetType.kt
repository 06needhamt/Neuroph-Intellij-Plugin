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
package com.thomas.needham.neurophidea.project.facet

import com.intellij.facet.Facet
import com.intellij.facet.FacetType
import com.intellij.facet.FacetTypeId
import com.intellij.openapi.module.JavaModuleType
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleType
import javax.swing.Icon

/**
 * Created by thoma on 07/06/2016.
 */
class NeurophFacetType : FacetType<NeurophFacet,NeurophFacetConfiguration> {
    companion object ID{
        @JvmStatic val FACET_TYPE_ID : FacetTypeId<NeurophFacet> = FacetTypeId<NeurophFacet>("Neuroph")
        @JvmStatic fun getInstance() : NeurophFacetType?{
            val cls = NeurophFacetType::class.java
            return findInstance(cls)
        }
    }
    constructor() : super(FACET_TYPE_ID,NeurophFacetSettings.FACET_ID,NeurophFacetSettings.FACET_NAME){

    }

    override fun createDefaultConfiguration() : NeurophFacetConfiguration? {
        val config = NeurophFacetConfiguration()
        return config
    }

    override fun createFacet(p0 : Module, p1 : String?, p2 : NeurophFacetConfiguration, p3 : Facet<*>?) : NeurophFacet? {
        val facet = NeurophFacet(this,p0,p1!!,p2,p3 as Facet<NeurophFacetConfiguration>)
        return facet
    }

    override fun isSuitableModuleType(p0 : ModuleType<*>?) : Boolean {
        return p0 is JavaModuleType
    }

    override fun getIcon() : Icon? {
        return super.getIcon()
    }
}