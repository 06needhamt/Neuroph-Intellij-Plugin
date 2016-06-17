package com.thomas.needham.neurophidea.designer.editor

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.thomas.needham.neurophidea.designer.psi.NnetFileType
import org.jetbrains.annotations.NonNls

/**
 * Created by thoma on 15/06/2016.
 */
class NnetFileEditorProvider : FileEditorProvider {

    companion object Data {
        @JvmStatic val LOG = Logger.getInstance("#com.thomas.needham.neurophidea.designer.editor.NnetFileEditorProvider")
        @JvmStatic val NNET_EDITOR_KEY : Key<NnetEditor> = Key.create("nnetEditor")
        @JvmStatic @NonNls val TYPE_ID : String = "nnet-editor"
        @JvmStatic @NonNls val LINE_ATTR : String = "line"
        @JvmStatic @NonNls val COLUMN_ATTR : String = "column"
        @JvmStatic @NonNls val SELECTION_START_LINE_ATTR : String = "selection-start-line"
        @JvmStatic @NonNls val SELECTION_START_COLUMN_ATTR : String = "selection-start-column"
        @JvmStatic @NonNls val SELECTION_END_LINE_ATTR : String = "selection-end-line"
        @JvmStatic @NonNls val SELECTION_END_COLUMN_ATTR : String = "selection-end-column"
        @JvmStatic @NonNls val RELATIVE_CARET_POSITION_ATTR : String = "relative-caret-position"
        @JvmStatic @NonNls val CARET_ELEMENT : String = "caret"
        @JvmStatic fun GetInstance() : NnetFileEditorProvider? {
            return ApplicationManager.getApplication().getComponent(NnetFileEditorProvider::class.java)
        }

        fun putNnetEditor(editor : Editor?, nnetEditor : NnetEditorImpl?) {
            editor?.putUserData(NNET_EDITOR_KEY, nnetEditor)
        }
    }

    override fun createEditor(p0 : Project, p1 : VirtualFile) : FileEditor {
        LOG.assertTrue(accept(p0, p1))
        return NnetEditorImpl(p0, p1, this)
    }

    override fun getPolicy() : FileEditorPolicy {
        return FileEditorPolicy.NONE
    }

    override fun getEditorTypeId() : String {
        return TYPE_ID
    }

    override fun accept(p0 : Project, p1 : VirtualFile) : Boolean {
        return p1.fileType is NnetFileType
    }
}