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

import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException

/**
 * Created by thoma on 06/06/2016.
 */
class RunWriteActionRunnable : Runnable{
    var builder : NeurophModuleBuilder?
    var file : VirtualFile?
    var module : Module?
    constructor(builder: NeurophModuleBuilder?, file: VirtualFile?, module: Module?){
        this.builder = builder
        this.file = file
        this.module = module
    }
    override fun run() {
        try{
            val test : VirtualFile? = file?.createChildDirectory(this,"test")
            if(test != null){
                val testSrc : VirtualFile? = test.createChildDirectory(this,"neuroph")
                val rootModel = ModuleRootManager.getInstance(module!!).modifiableModel
                val entry : ContentEntry? = NeurophModuleBuilder.findContentEntry(rootModel,testSrc)
                if(entry != null){
                    entry.addSourceFolder(testSrc!!,true)
                    rootModel.commit()
                }
            }
        }
        catch(ioe: IOException){
            ioe.printStackTrace(System.err)
            Messages.showErrorDialog(null,"Error Creating Project","Error")
        }
    }
}