/*
    The MIT License (MIT)
    
    neuroph-plugin Copyright (c) 2016 thoma
    
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

package com.thomas.needham.neurophidea.core

import com.intellij.ide.DataManager
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.actionSystem.DataConstants
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileCopyEvent
import com.intellij.openapi.vfs.VirtualFileEvent
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileMoveEvent
import com.intellij.openapi.vfs.VirtualFilePropertyEvent
import com.thomas.needham.neurophidea.Constants
import com.thomas.needham.neurophidea.PluginRegistration

/**
 * Created by thoma on 26/09/2016.
 */
class FileSaveListener : VirtualFileListener {
    companion object Data {
        @JvmStatic val properties = PropertiesComponent.getInstance()
        @JvmStatic var enumerateFiles : (VirtualFile?) -> MutableList<VirtualFile?> = { directory ->
            mutableListOf(directory)
        }
        @JvmStatic var files : List<VirtualFile?> = listOf()
        @JvmStatic val getProject : () -> Project? = {
            val dataContext = DataManager.getInstance().getDataContext()
            val project : Project? = dataContext.getData(DataConstants.PROJECT) as Project?
            project
        }
        inline @JvmStatic fun getNetworksInProject() : List<VirtualFile?> {
            files = mutableListOf()
            val project : Project? = getProject.invoke()
            if (project == null)
                return listOf<VirtualFile?>()
            files += enumerateFiles(project.baseDir!!)
            files.filter { e ->
                !e?.isDirectory!!
            }
            return files
        }
        @JvmStatic val checkFileNames : (List<VirtualFile?>, String) -> Boolean = { files, name ->
            var found : Boolean = false
            for (file : VirtualFile? in files) {
                found = file?.name?.equals(name)!!
            }
            found
        }
        inline @JvmStatic fun isCodeGenerated(project: Project?) : Boolean {
            val temp = files

            for(file: VirtualFile? in temp){
                if(properties.getBoolean(Constants.GENERATE_JAVA_KEY) && temp.any {
                    e -> e?.name?.equals(file?.name)!! && e?.extension?.equals("java")!! }) {
                    return true
                }
                if(properties.getBoolean(Constants.GENERATE_GROOVY_KEY) && temp.any {
                    e -> e?.name?.equals(file?.name)!! && e?.extension?.equals("groovy")!! }) {
                    return true
                }
                if(properties.getBoolean(Constants.GENERATE_SCALA_KEY) && temp.any {
                    e -> e?.name?.equals(file?.name)!! && e?.extension?.equals("scala")!! }) {
                    return true
                }
                if(properties.getBoolean(Constants.GENERATE_KOTLIN_KEY) && temp.any {
                    e -> e?.name?.equals(file?.name)!! && e?.extension?.equals("kt")!! }) {
                    return true
                }
            }
            return false
        }
    }

    override fun beforePropertyChange(p0 : VirtualFilePropertyEvent) {

    }

    override fun beforeContentsChange(p0 : VirtualFileEvent) {

    }

    override fun fileDeleted(p0 : VirtualFileEvent) {

    }

    override fun beforeFileMovement(p0 : VirtualFileMoveEvent) {

    }

    override fun fileMoved(p0 : VirtualFileMoveEvent) {

    }

    override fun propertyChanged(p0 : VirtualFilePropertyEvent) {

    }

    override fun contentsChanged(p0 : VirtualFileEvent) {
        enumerateFiles = { directory ->
            val found : MutableList<VirtualFile?> = mutableListOf()
            for (file : VirtualFile? in directory?.children!!) {
                if (file?.isDirectory!!) {
                    files += enumerateFiles(file)
                } else if (!file?.isDirectory!! && (file?.extension.equals("conf") || file?.extension.equals("nnet"))) {
                    found.add(file)
                }
            }
            found
        }
        files = getNetworksInProject()
        if ((p0.isFromSave && checkFileNames(files, p0.fileName)) || !isCodeGenerated(getProject.invoke())) {
            // Only Regenerate Files If They Have Been Modified Or Do Not Exist
            val generator : AutoCodeGenerator = AutoCodeGenerator()
            if (!generator.GenerateCode() && generator.error && !PluginRegistration.projectClosing)
                Messages.showErrorDialog(AutoCodeGenerator.project, "Error Generating Code", "Error")
        }
        files = mutableListOf()
    }

    override fun beforeFileDeletion(p0 : VirtualFileEvent) {

    }

    override fun fileCreated(p0 : VirtualFileEvent) {

    }

    override fun fileCopied(p0 : VirtualFileCopyEvent) {

    }
}