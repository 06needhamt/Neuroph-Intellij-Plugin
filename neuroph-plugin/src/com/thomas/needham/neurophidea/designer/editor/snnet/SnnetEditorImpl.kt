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

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.TextEditorLocation
import com.intellij.openapi.fileEditor.impl.text.AsyncEditorLoader
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.pom.Navigatable
import org.jetbrains.annotations.NotNull
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import javax.swing.JComponent

/**
 * Created by thoma on 24/06/2016.
 */
open class SnnetEditorImpl : UserDataHolderBase, SnnetEditor {
    val project : Project?
    val changeSupport : PropertyChangeSupport?
    @NotNull val component : SnnetEditorComponent?
    @NotNull val file: VirtualFile?
    val asyncLoader : AsyncEditorLoader? = null

    constructor(@NotNull project: Project?, @NotNull file: VirtualFile?, provider: SnnetFileEditorProvider?) : super(){
        this.project = project
        this.file = file
        this.changeSupport = PropertyChangeSupport(this)
        this.component = CreateEditorComponent(project!!,file!!)
    }
    @NotNull
    protected fun loadEditorInBackground() : Runnable{
        val scheme = EditorColorsManager.getInstance().globalScheme
        val highLighter = EditorHighlighterFactory.getInstance().createEditorHighlighter(file!!,scheme,project)
        val editor : EditorEx? = getEditor() as EditorEx
        highLighter.setText(editor?.document?.immutableCharSequence!! as String)
        val run : Runnable = { editor : EditorEx -> editor.highlighter = highLighter } as Runnable
        return run
    }

    private fun CreateEditorComponent(project : Project, file : VirtualFile) : SnnetEditorComponent {
        return SnnetEditorComponent(project,file,this)
    }

    override fun getEditor() : Editor? {
        return GetActiveEditor() as Editor?
    }

    override fun getName() : String {
        return "Snnet Editor"
    }

    override fun setState(p0 : FileEditorState) {

    }

    override fun getComponent() : JComponent {
        return component!!
    }

    override fun getPreferredFocusedComponent() : JComponent? {
        return GetActiveEditor()
    }

    private fun  GetActiveEditor() : JComponent? {
        return component?.editor as JComponent?
    }

    override fun deselectNotify() {

    }

    override fun getBackgroundHighlighter() : BackgroundEditorHighlighter? {
        return null
    }

    override fun isValid() : Boolean {
        return component?.isComponentValid!!
    }

    override fun isModified() : Boolean {
        return component?.isComponentModified!!
    }

    override fun addPropertyChangeListener(p0 : PropertyChangeListener) {
        changeSupport?.addPropertyChangeListener(p0)
    }

    override fun selectNotify() {
        component?.selectNotify()
    }

    override fun getCurrentLocation() : FileEditorLocation? {
        return TextEditorLocation(getEditor()?.caretModel?.logicalPosition!!, this as TextEditor)
    }

    override fun removePropertyChangeListener(p0 : PropertyChangeListener) {
        changeSupport?.removePropertyChangeListener(p0)
    }

    override fun navigateTo(p0 : Navigatable) {
        (p0 as OpenFileDescriptor).navigateIn(getEditor()!!)
    }

    override fun canNavigateTo(p0 : Navigatable) : Boolean {
        return p0 is OpenFileDescriptor && ((p0 as OpenFileDescriptor).line != 1 || (p0 as OpenFileDescriptor).offset != 0)
    }

    override fun <T : Any?> putUserData(p0 : Key<T>, p1 : T?) {
        throw UnsupportedOperationException()
    }

    override fun <T : Any?> getUserData(p0 : Key<T>) : T? {
        throw UnsupportedOperationException()
    }

    override fun dispose() {
        component?.dispose()
    }
    fun firePropertyChange(propertyName: String, oldValue: Any?, newValue: Any?) {
        changeSupport?.firePropertyChange(propertyName, oldValue, newValue);
    }

    override fun toString() : String {
        return "Snnet Editor Implementation"
    }

    fun updateModifiedProperty() {
        component?.updateModifiedProperty()
    }
}