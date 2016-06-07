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

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.projectRoots.AdditionalDataConfigurable
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.projectRoots.SdkAdditionalData
import com.intellij.openapi.projectRoots.SdkModel
import com.intellij.openapi.projectRoots.SdkModificator
import com.intellij.openapi.projectRoots.SdkType
import com.intellij.openapi.projectRoots.SdkTypeId
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.xmlb.XmlSerializer
import com.sun.org.apache.xml.internal.serialize.XMLSerializer
import com.thomas.needham.neurophidea.Constants.VERSION_KEY
import org.jdom.Element
import org.jetbrains.annotations.Nullable
import java.io.File
import javax.swing.Icon

/**
 * Created by thoma on 06/06/2016.
 */
class NeurophSDK : SdkType("Neuroph SDK") {

    companion object Data{
        var properties = PropertiesComponent.getInstance()
        @JvmStatic fun getInstance() : SdkTypeId?{
            val cls = NeurophSDK::class.java
            return SdkType.findInstance(cls)
        }
    }
    override fun getPresentableName() : String {
        return "Neuroph SDK"
    }

    override fun isValidSdkHome(p0 : String?) : Boolean {
        val home = LocalFileSystem.getInstance().findFileByIoFile(File(p0));
        if (home != null && home.exists() && home.isDirectory()) {
            val neuroph = home.findChild("neuroph");
            val lib = home.findChild("lib");
            if (neuroph != null && neuroph.name.contains("neuroph") && lib != null && lib.isDirectory) {
                return true;
            }
        }
        return false
    }

    override fun suggestSdkName(p0 : String?, p1 : String?) : String? {
        return "Neuroph SDK"
    }

    override fun suggestHomePath() : String? {
        val getPath : () -> String? = {
            if(!properties.getValue(VERSION_KEY,"").equals(""))
                properties.getValue(VERSION_KEY,"")
            else if(System.getenv("NEUROPH_HOME") != null)
                System.getenv("NEUROPH_HOME")
            else
                ""
        }
        return getPath()
    }

    override fun createAdditionalDataConfigurable(p0 : SdkModel, p1 : SdkModificator) : AdditionalDataConfigurable? {
        return null
    }

    override fun loadAdditionalData(additional : Element?) : SdkAdditionalData? {
        val cls = NeurophSDKData::class.java
        return XmlSerializer.deserialize(additional!!, cls);
    }

    override fun getIcon() : Icon? {
        return super.getIcon()
    }

    override fun getIconForAddAction() : Icon {
        return super.getIconForAddAction()
    }
    override fun saveAdditionalData(p0 : SdkAdditionalData, p1 : Element) {
        if(p0 is NeurophSDKData)
            XmlSerializer.serializeInto(p0, p1);
    }

    @Nullable
    override fun getVersionString(sdk : Sdk) : String? {
        val path : String? = sdk.homePath
        if (path == null) return null;
        val file : File = File(path)
        val home : VirtualFile? = LocalFileSystem.getInstance().findFileByIoFile(file)
        if(home != null){
            for(jar : VirtualFile in home.children){
                if(jar.name.startsWith("neuroph") && jar.extension.equals("jar")) {
                    var name = jar.name
                    name = name.substring(0..name.length - 5)
                    name = name.replace(".jar", "");
                    return name
                }
            }
        }
        return null
    }
}