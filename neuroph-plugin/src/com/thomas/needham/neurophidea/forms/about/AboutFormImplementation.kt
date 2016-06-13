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
@file:JvmName("AboutForm\$Ext") // Do Some Kotlin Sorcery!
@file:JvmMultifileClass() // Do Some Kotlin Sorcery!
package com.thomas.needham.neurophidea.forms.about

import com.intellij.ide.IdeBundle
import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.application.impl.ApplicationInfoImpl
import com.intellij.openapi.extensions.PluginId

/**
 * Created by thoma on 13/06/2016.
 */

fun AboutForm.DisplayPluginVersion(){
    lblVersion.text += PluginManager.getPlugin(PluginId.getId("com.thomas.needham.neurophidea"))?.getVersion();
}

fun AboutForm.DisplayIDEVersion(){
    val appInfo : ApplicationInfoImpl? = ApplicationInfoEx.getInstance() as ApplicationInfoImpl?
    val buildinfo : String = IdeBundle.message("about.box.build.number",appInfo?.build.toString())
    lblRunningOn.text += buildinfo
}