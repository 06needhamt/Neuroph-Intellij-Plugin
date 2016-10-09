package com.thomas.needham.neurophidea.designer.editor.tset

import com.intellij.openapi.fileTypes.BinaryFileDecompiler
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.thomas.needham.neurophidea.actions.InitialisationAction
import org.neuroph.core.learning.TrainingElement
import org.neuroph.core.learning.TrainingSet
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Created by thoma on 12/07/2016.
 */
class TsetDecompiler : BinaryFileDecompiler {
    var dataSetLoader : TsetEditorLoader? = null
    var dataSet : TrainingSet<TrainingElement?>? = null

    override fun decompile(p0 : VirtualFile?) : CharSequence {
        try {
            var result = ""
            if (p0?.isValid!!) {
                dataSetLoader = TsetEditorLoader(p0)
                dataSet = dataSetLoader?.LoadDataSet()
                if (dataSet == null) {
                    throw IOException("Error Loading Data Set From File: ${p0?.path}")
                }
                for (i in 0..dataSet?.elements()?.size!! - 1) {
                    val inarr = dataSet?.elements()!![i]?.inputArray
                    val outarr = dataSet?.elements()!![i]?.idealArray
                    val supervised = dataSet?.elements()!![i]?.isSupervised
                    result += ParseInput(inarr) + ","
                    result += ParseOutput(outarr, supervised!!) + ","
                    result += "\n"
                }
                return result
            } else {
                throw FileNotFoundException("File: ${p0?.path} Does Not Exist")
            }
        } catch(ioe : IOException) {
            ioe.printStackTrace(System.err)
            Messages.showErrorDialog(InitialisationAction.project, "${ioe.message}", "Error")
            return ""
        } catch(fnfe : FileNotFoundException) {
            fnfe.printStackTrace(System.err)
            Messages.showErrorDialog(InitialisationAction.project, "${fnfe.message}", "Error")
            return ""
        }
    }

    private fun ParseOutput(arr : DoubleArray?, supervised : Boolean) : String {
        var result = ""
        if (supervised) {
            for (i in 0..arr?.size!! - 1) {
                if (i == arr?.size!! - 1)
                    result += arr!![i].toString()
                else
                    result += arr!![i].toString() + ","
            }
            return result
        } else {
            for (i in 0..arr?.size!! - 1) {
                if (i == arr?.size!! - 1)
                    result += (arr!![i] / Math.log(3.0)).toString()
                else
                    result += (arr!![i] / Math.log(3.0)).toString() + ","
            }
            return result
        }
    }

    private fun ParseInput(arr : DoubleArray?) : String {
        var result = ""
        for (i in 0..arr?.size!! - 1) {
            if (i == arr?.size!! - 1)
                result += arr!![i].toString()
            else
                result += arr!![i].toString() + ","
        }
        return result
    }
}
