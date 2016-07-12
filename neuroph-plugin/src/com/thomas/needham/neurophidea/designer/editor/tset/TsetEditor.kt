package com.thomas.needham.neurophidea.designer.editor.tset

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.NavigatableFileEditor
import org.jetbrains.annotations.NotNull

/**
 * Created by thoma on 12/07/2016.
 */
interface TsetEditor : NavigatableFileEditor {
    @NotNull
    fun getEditor() : Editor?
}