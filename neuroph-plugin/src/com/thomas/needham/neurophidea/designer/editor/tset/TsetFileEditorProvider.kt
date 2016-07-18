package com.thomas.needham.neurophidea.designer.editor.tset

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.thomas.needham.neurophidea.designer.psi.tset.TsetFileType
import org.jetbrains.annotations.NonNls

/**
 * Created by thoma on 12/07/2016.
 */
class TsetFileEditorProvider : FileEditorProvider{
    companion object Data {
        @JvmStatic val LOG = Logger.getInstance("#com.thomas.needham.neurophidea.designer.editor.tset.TsetFileEditorProvider")
        @JvmStatic val TSET_EDITOR_KEY : Key<TsetEditor> = Key.create("tsetEditor")
        @JvmStatic @NonNls val TYPE_ID : String = "tset-editor"
        @JvmStatic @NonNls val LINE_ATTR : String = "line"
        @JvmStatic @NonNls val COLUMN_ATTR : String = "column"
        @JvmStatic @NonNls val SELECTION_START_LINE_ATTR : String = "selection-start-line"
        @JvmStatic @NonNls val SELECTION_START_COLUMN_ATTR : String = "selection-start-column"
        @JvmStatic @NonNls val SELECTION_END_LINE_ATTR : String = "selection-end-line"
        @JvmStatic @NonNls val SELECTION_END_COLUMN_ATTR : String = "selection-end-column"
        @JvmStatic @NonNls val RELATIVE_CARET_POSITION_ATTR : String = "relative-caret-position"
        @JvmStatic @NonNls val CARET_ELEMENT : String = "caret"
        @JvmStatic fun GetInstance() : TsetFileEditorProvider? {
            return ApplicationManager.getApplication().getComponent(TsetFileEditorProvider::class.java)
        }
    }
    fun putTsetEditor(editor: Editor?, tsetEditor : TsetEditor){
        editor?.putUserData(TSET_EDITOR_KEY,tsetEditor)
    }
    override fun createEditor(p0 : Project, p1 : VirtualFile) : FileEditor {
        LOG.assertTrue(accept(p0,p1))
        return TsetEditorImpl(p0,p1,this)
    }

    override fun getPolicy() : FileEditorPolicy {
        return FileEditorPolicy.NONE
    }

    override fun getEditorTypeId() : String {
        return TYPE_ID
    }

    override fun accept(p0 : Project, p1 : VirtualFile) : Boolean {
        return p1.fileType is TsetFileType
    }
}