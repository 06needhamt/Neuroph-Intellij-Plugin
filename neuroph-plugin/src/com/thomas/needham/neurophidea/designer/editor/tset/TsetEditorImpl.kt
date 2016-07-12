package com.thomas.needham.neurophidea.designer.editor.tset

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.impl.text.AsyncEditorLoader
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.pom.Navigatable
import org.jetbrains.annotations.NotNull
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport
import javax.swing.JComponent

/**
 * Created by thoma on 12/07/2016.
 */
class TsetEditorImpl : UserDataHolderBase, TsetEditor {
    val project : Project?
    val changeSupport : PropertyChangeSupport?
    @NotNull val component : ?
    @NotNull val file: VirtualFile?
    val asyncLoader : AsyncEditorLoader? = null

    constructor(@NotNull project: Project?, @NotNull file: VirtualFile?, provider: ?) : super(){
        this.project = project
        this.file = file
        this.changeSupport = PropertyChangeSupport(this)
        this.component = CreateEditorComponent(project!!,file!!)
    }

    private fun  CreateEditorComponent(project : Project, file : VirtualFile) : ? {
    }

    override fun getEditor() : Editor {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getName() : String {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setState(p0 : FileEditorState) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getComponent() : JComponent {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPreferredFocusedComponent() : JComponent {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deselectNotify() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBackgroundHighlighter() : BackgroundEditorHighlighter {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isValid() : Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isModified() : Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addPropertyChangeListener(p0 : PropertyChangeListener) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun selectNotify() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentLocation() : FileEditorLocation {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removePropertyChangeListener(p0 : PropertyChangeListener) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateTo(p0 : Navigatable) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun canNavigateTo(p0 : Navigatable) : Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dispose() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}