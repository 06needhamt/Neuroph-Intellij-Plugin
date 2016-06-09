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

import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.ui.ComboboxWithBrowseButton
import javax.swing.DefaultComboBoxModel
import javax.swing.ListCellRenderer

/**
 * Created by Thomas Needham on 06/06/2016.
 */
class NeurophSDKComboBox : ComboboxWithBrowseButton {
    companion object Data{
        var cellRenderer : NeurophSDKCellRenderer? = null
        var listener : NeurophSDKComboBoxActionListener? = null
    }
    constructor(){
        cellRenderer = NeurophSDKCellRenderer()
        listener = NeurophSDKComboBoxActionListener()
        this.comboBox.renderer = cellRenderer as ListCellRenderer<in Any>
        listener?.instance = this
        this.addActionListener(listener)
        updateSDKList(getSelectedSdk(),false)
    }

    fun getSelectedSdk() : Sdk? {
        return this.comboBox.selectedItem as Sdk?
    }

    fun updateSDKList(sdk : Sdk?, any : Boolean) {
        val sdkList : MutableList<Sdk?> = ProjectJdkTable.getInstance().getSdksOfType(NeurophSDK.getInstance())
        var sdkToSelect = sdk
        if(any && sdkList.size > 0){
            sdkToSelect = sdkList.elementAt(0)
            comboBox.model = DefaultComboBoxModel(sdkList.toTypedArray())
            comboBox.selectedItem = sdkToSelect
        }
    }
}