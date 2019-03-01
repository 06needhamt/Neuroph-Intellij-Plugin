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

import com.intellij.ide.util.projectWizard.WizardInputField
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.Sdk
import com.thomas.needham.neurophidea.Constants.SDK_NAME

/**
 * Created by Thomas Needham on 07/06/2016.
 */
open class NeurophWizardInputField : WizardInputField<NeurophSDKComboBox> {
	companion object Data {
		var combo: NeurophSDKComboBox? = null
		@JvmStatic
		fun findMostRecentSdkPath(): String? {
			val sdk: Sdk? = ProjectJdkTable.getInstance().findMostRecentSdk(NeurophSDKCondition())
			return if (sdk != null) sdk.name else null
		}
	}
	
	constructor() : super(SDK_NAME, findMostRecentSdkPath()) {
		combo = NeurophSDKComboBox()
	}
	
	override fun getLabel(): String? {
		return "Neuroph SDK"
	}
	
	override fun getComponent(): NeurophSDKComboBox? {
		return combo
	}
	
	override fun getValue(): String? {
		val sdk: Sdk? = combo?.getSelectedSdk()
		return if (sdk == null) "" else sdk.homePath
	}
}