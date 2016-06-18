package com.thomas.needham.neurophidea.designer.psi

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.PlatformIcons
import javax.swing.Icon

/**
 * Created by thoma on 15/06/2016.
 */
class DataSetFileType : FileType {
    override fun getIcon() : Icon? {
        return PlatformIcons.FILE_ICON
    }

    override fun getName() : String {
        return "Neural Network Training / Testing Set"
    }

    override fun isBinary() : Boolean {
        return false
    }

    override fun isReadOnly() : Boolean {
        return false
    }

    override fun getDefaultExtension() : String {
        return "csv"
    }

    override fun getCharset(p0 : VirtualFile, p1 : ByteArray) : String? {
        throw UnsupportedOperationException()
    }

    override fun getDescription() : String {
        return "Comma Separated Data Set Used For" +
                "Training and Testing Neural Networks"
    }
}