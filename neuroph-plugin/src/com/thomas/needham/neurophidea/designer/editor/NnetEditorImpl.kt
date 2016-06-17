package com.thomas.needham.neurophidea.designer.editor

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.highlighter.EditorHighlighterFactory
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
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
import com.intellij.ui.components.JBLoadingPanel

/**
 * Created by thoma on 17/06/2016.
 */
open class NnetEditorImpl : UserDataHolderBase, NnetEditor{
    val project : Project?
    val changeSupport : PropertyChangeSupport?
    @NotNull val component : NnetEditorComponent?
    @NotNull val file: VirtualFile?
    val asyncLoader : AsyncEditorLoader? = null

    constructor(@NotNull project: Project?, @NotNull file: VirtualFile?, provider: NnetFileEditorProvider?) : super(){
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
        return { editor : EditorEx -> editor.highlighter = highLighter } as Runnable
    }
    private fun CreateEditorComponent(project : Project, file : VirtualFile) : NnetEditorComponent? {
        return NnetEditorComponent(project,file,this)
    }

    override fun getEditor() : Editor? {
        return GetActiveEditor() as Editor?
    }

    override fun getName() : String {
        return "Nnet Editor"
    }

    override fun setState(p0 : FileEditorState) {
        throw UnsupportedOperationException()
    }

    override fun getComponent() : JComponent {
        return component!!;
    }

    override fun getPreferredFocusedComponent() : JComponent? {
        return GetActiveEditor()
    }

    private fun GetActiveEditor() : JComponent? {
        return component?.editor as JComponent?
    }

    override fun deselectNotify() {

    }

    override fun getBackgroundHighlighter() : BackgroundEditorHighlighter? {
        throw UnsupportedOperationException()
    }

    override fun isValid() : Boolean {
        throw UnsupportedOperationException()
    }

    override fun isModified() : Boolean {
        throw UnsupportedOperationException()
    }

    override fun addPropertyChangeListener(p0 : PropertyChangeListener) {
        throw UnsupportedOperationException()
    }

    override fun selectNotify() {
        component?.selectNotify()
    }

    override fun getCurrentLocation() : FileEditorLocation? {
        throw UnsupportedOperationException()
    }

    override fun removePropertyChangeListener(p0 : PropertyChangeListener) {
        throw UnsupportedOperationException()
    }

    override fun navigateTo(p0 : Navigatable) {
        throw UnsupportedOperationException()
    }

    override fun canNavigateTo(p0 : Navigatable) : Boolean {
        throw UnsupportedOperationException()
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
}