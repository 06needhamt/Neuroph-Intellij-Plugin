package com.thomas.needham.neurophidea.designer.editor

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.NavigatableFileEditor
import org.jetbrains.annotations.NotNull

/**
 * Created by thoma on 17/06/2016.
 */
interface  NnetEditor : NavigatableFileEditor{
    @NotNull
    fun getEditor() : Editor?
}