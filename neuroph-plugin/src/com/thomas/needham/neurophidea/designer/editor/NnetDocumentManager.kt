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
package com.thomas.needham.neurophidea.designer.editor

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.impl.FileDocumentManagerImpl
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.ex.DocumentEx
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.SingleRootFileViewProvider

/**
 * Created by thoma on 18/06/2016.
 */
class NnetDocumentManager : FileDocumentManagerImpl {
    companion object Instance{
        fun getInstance() : FileDocumentManager {
            return ApplicationManager.getApplication().getComponent(NnetDocumentManager::class.java)
        }
    }
    constructor(p0 : VirtualFileManager, p1 : ProjectManager) : super(p0, p1){

    }

    override fun getDocument(p0 : VirtualFile) : Document? {
        ApplicationManager.getApplication().assertReadAccessAllowed();
        val document = getCachedDocument(p0) as DocumentEx?;
        if (!p0.isValid || p0.isDirectory || SingleRootFileViewProvider.isTooLargeForContentLoading(p0)) {
            return null;
        } else {
            return document
        }
    }
}