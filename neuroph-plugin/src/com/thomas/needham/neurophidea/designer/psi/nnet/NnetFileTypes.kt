package com.thomas.needham.neurophidea.designer.psi.nnet

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.fileTypes.FileTypes

/**
 * Created by thoma on 15/06/2016.
 */
class NnetFileTypes : FileTypes {
    private constructor() : super(){

    }
    companion object FileTypes {
        @JvmStatic val NEURAL_NETWORK : FileType = FileTypeManager.getInstance().getFileTypeByExtension("nnet")
    }
}