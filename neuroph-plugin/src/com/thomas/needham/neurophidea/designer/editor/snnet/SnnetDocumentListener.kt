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
package com.thomas.needham.neurophidea.designer.editor.snnet

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.event.DocumentAdapter
import com.intellij.openapi.editor.event.DocumentEvent

/**
 * Created by thoma on 24/06/2016.
 */
class SnnetDocumentListener : DocumentAdapter, Runnable {
    var updateScheduled : Boolean = false
    var component : SnnetEditorComponent? = null
    private val lambda : () -> Unit = {
        updateScheduled = false
        this.component?.updateModifiedProperty()
    }
    constructor(component : SnnetEditorComponent){
        this.component = component
    }
    override fun run() {
        lambda()
    }
    override fun documentChanged(e : DocumentEvent?) {
        super.documentChanged(e)
        if(!updateScheduled){
            ApplicationManager.getApplication().invokeLater(this)
            updateScheduled = true
        }
    }
}