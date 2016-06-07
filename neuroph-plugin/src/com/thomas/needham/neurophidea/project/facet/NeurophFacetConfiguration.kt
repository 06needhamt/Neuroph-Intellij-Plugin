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

import com.intellij.facet.FacetConfiguration
import com.intellij.facet.ui.FacetEditorContext
import com.intellij.facet.ui.FacetEditorTab
import com.intellij.facet.ui.FacetValidatorsManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jdom.Element
import org.jetbrains.annotations.NotNull

/**
 * Created by thoma on 06/06/2016.
 */
class NeurophFacetConfiguration : FacetConfiguration, PersistentStateComponent<NeurophFacetSettings> {
     companion object Data{
        var settings : NeurophFacetSettings? = NeurophFacetSettings()
    }

    @Deprecated("No Longer in Use")
    override fun readExternal(p0 : Element?) {
    }

    override fun createEditorTabs(p0 : FacetEditorContext?, p1 : FacetValidatorsManager?) : Array<out FacetEditorTab>? {
        return arrayOf(NeurophFacetEditorTab(settings))
    }
    @Deprecated("No Longer in Use")
    override fun writeExternal(p0 : Element?) {

    }
@NotNull
    override fun getState() : NeurophFacetSettings? {
        return settings
    }

    override fun loadState(p0 : NeurophFacetSettings?) {
        XmlSerializerUtil.copyBean(state!!,settings!!)
    }

    fun setSdk(sdk: Sdk?) : Unit{
        settings?.neurophSDKName = sdk?.name

    }
}