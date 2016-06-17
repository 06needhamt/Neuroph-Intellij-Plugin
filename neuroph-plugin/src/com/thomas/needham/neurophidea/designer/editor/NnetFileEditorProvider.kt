package com.thomas.needham.neurophidea.designer.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.diagnostic.Logger

/**
 * Created by thoma on 15/06/2016.
 */
class NnetFileEditorProvider : FileEditorProvider {
    companion object Log{
        @JvmStatic val LOG = Logger.getInstance("#com.thomas.needham.neurophidea.designer.editor.NnetFileEditorProvider");
        @JvmStatic val  NNET_EDITOR_KEY : Key<NnetEditor> = Key.create("nnetEditor");
    }
    override fun createEditor(p0 : Project, p1 : VirtualFile) : FileEditor {
        throw UnsupportedOperationException()
    }

    override fun getPolicy() : FileEditorPolicy {
        throw UnsupportedOperationException()
    }

    override fun getEditorTypeId() : String {
        throw UnsupportedOperationException()
    }

    override fun accept(p0 : Project, p1 : VirtualFile) : Boolean {
        throw UnsupportedOperationException()
    }
}