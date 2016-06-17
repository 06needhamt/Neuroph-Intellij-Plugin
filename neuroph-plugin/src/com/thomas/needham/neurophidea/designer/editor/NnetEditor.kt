package com.thomas.needham.neurophidea.designer.editor

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.util.Key
import java.beans.PropertyChangeListener
import javax.swing.JComponent

/**
 * Created by thoma on 17/06/2016.
 */
class NnetEditor : FileEditor {
    override fun getName() : String {
        throw UnsupportedOperationException()
    }

    override fun setState(p0 : FileEditorState) {
        throw UnsupportedOperationException()
    }

    override fun getComponent() : JComponent {
        throw UnsupportedOperationException()
    }

    override fun getPreferredFocusedComponent() : JComponent? {
        throw UnsupportedOperationException()
    }

    override fun deselectNotify() {
        throw UnsupportedOperationException()
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
        throw UnsupportedOperationException()
    }

    override fun getCurrentLocation() : FileEditorLocation? {
        throw UnsupportedOperationException()
    }

    override fun removePropertyChangeListener(p0 : PropertyChangeListener) {
        throw UnsupportedOperationException()
    }

    override fun <T : Any?> putUserData(p0 : Key<T>, p1 : T?) {
        throw UnsupportedOperationException()
    }

    override fun <T : Any?> getUserData(p0 : Key<T>) : T? {
        throw UnsupportedOperationException()
    }

    override fun dispose() {
        throw UnsupportedOperationException()
    }
}