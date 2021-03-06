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
@file:JvmName("ExportNetworkForm\$Ext") // Do Some Kotlin Sorcery!
@file:JvmMultifileClass()

// Do Some Kotlin Sorcery!
package com.thomas.needham.neurophidea.forms.export

/**
 * Created by Thomas Needham on 29/05/2016.
 */

fun ExportNetworkForm.AddOnClickListeners() {
	val networkListener: ExportNetworkBrowseButtonActionListener? = ExportNetworkBrowseButtonActionListener()
	networkListener?.formInstance = this
	btnBrowseExport?.addActionListener(networkListener)
	val exportSourceListener: ExportSourceBrowseButtonActionListener? = ExportSourceBrowseButtonActionListener()
	exportSourceListener?.formInstance = this
	btnExportSource?.addActionListener(exportSourceListener)
	val exportListener: ExportSourceButtonActionListener? = ExportSourceButtonActionListener()
	exportListener?.formInstance = this
	btnExport?.addActionListener(exportListener)
}