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

import com.intellij.facet.ui.FacetEditorTab
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.util.text.StringUtil
import com.thomas.needham.neurophidea.project.NeurophSDKPanel
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.Nullable
import javax.naming.ConfigurationException
import javax.swing.JComponent

/**
 * Created by Thomas Needham on 06/06/2016.
 */
class NeurophFacetEditorTab : FacetEditorTab {
    companion object Data{
        var panel : NeurophSDKPanel? = null
        var settings : NeurophFacetSettings? = null
    }
    constructor(s: NeurophFacetSettings?){
        settings = s
        panel = NeurophSDKPanel()
    }
    override fun isModified() : Boolean {
        return !StringUtil.equals(settings?.neurophSDKName,panel?.sdkName)
    }

    override fun disposeUIResources() {

    }

    override fun reset() {
        val sdk = ProjectJdkTable.getInstance().findJdk(settings?.neurophSDKName)
    }
    @Nls
    override fun getDisplayName() : String? {
        return "Neuroph"
    }
@Nullable
    override fun createComponent() : JComponent {
        return panel?.root as JComponent
    }
@Throws(ConfigurationException::class)
    override fun apply() {
        settings?.neurophSDKName = panel?.sdkName
    }
}