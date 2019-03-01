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
package com.thomas.needham.neurophidea.designer.psi.tset

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

/**
 * Created by thoma on 15/06/2016.
 */
class TsetFileType : FileType {
	override fun getIcon(): Icon? {
		return IconLoader.getTransparentIcon(IconLoader.getIcon("/icon2.png"))
	}
	
	override fun getName(): String {
		return "Neuroph Training / Testing Set"
	}
	
	override fun isBinary(): Boolean {
		return true
	}
	
	override fun isReadOnly(): Boolean {
		return false
	}
	
	override fun getDefaultExtension(): String {
		return "tset"
	}
	
	override fun getCharset(p0: VirtualFile, p1: ByteArray): String? {
		throw UnsupportedOperationException()
	}
	
	override fun getDescription(): String {
		return "Data Set Used For Training and Testing Neural Networks"
	}
}