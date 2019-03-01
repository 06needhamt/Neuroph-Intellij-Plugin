/*
The MIT License (MIT)

Copyright (c) 2016 Tom Needham

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.thomas.needham.neurophidea.designer.editor.nnet

import com.intellij.openapi.fileTypes.BinaryFileDecompiler
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.thomas.needham.neurophidea.actions.InitialisationAction
import org.neuroph.core.NeuralNetwork
import org.neuroph.core.transfer.*
import org.neuroph.nnet.learning.*
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Created by thoma on 20/06/2016.
 */
class NnetDecompiler : BinaryFileDecompiler {
	var networkLoader: NnetLoader? = null
	var network: NeuralNetwork? = null
	
	override fun decompile(p0: VirtualFile?): CharSequence {
		try {
			if (p0?.isValid!!) {
				networkLoader = NnetLoader(p0)
				network = networkLoader?.LoadNetwork()
				if (network == null)
					throw IOException("Error Loading Network From File: ${p0?.path}")
				var result = ""
				result += "Network Type: ${network?.networkType?.name}" + "\n"
				result += "Learning Rule: "
				when (network?.learningRule) {
					is AntiHebbianLearning -> result += AntiHebbianLearning::class.java.simpleName + "\n"
					is BackPropagation -> result += BackPropagation::class.java.simpleName + "\n"
					is BinaryDeltaRule -> result += BinaryDeltaRule::class.java.simpleName + "\n"
					is CompetitiveLearning -> result += CompetitiveLearning::class.java.simpleName + "\n"
					is DynamicBackPropagation -> result += DynamicBackPropagation::class.java.simpleName + "\n"
					is GeneralizedHebbianLearning -> result += GeneralizedHebbianLearning::class.java.simpleName + "\n"
					is HopfieldLearning -> result += HopfieldLearning::class.java.simpleName + "\n"
					is InstarLearning -> result += InstarLearning::class.java.simpleName + "\n"
					is KohonenLearning -> result += KohonenLearning::class.java.simpleName + "\n"
					is LMS -> result += LMS::class.java.simpleName + "\n"
					is MomentumBackpropagation -> result += MomentumBackpropagation::class.java.simpleName + "\n"
					is OjaLearning -> result += OjaLearning::class.java.simpleName + "\n"
					is OutstarLearning -> result += OutstarLearning::class.java.simpleName + "\n"
					is PerceptronLearning -> result += PerceptronLearning::class.java.simpleName + "\n"
					is ResilientPropagation -> result += ResilientPropagation::class.java.simpleName + "\n"
					is SigmoidDeltaRule -> result += SigmoidDeltaRule::class.java.simpleName + "\n"
					is SimulatedAnnealingLearning -> result += SimulatedAnnealingLearning::class.java.simpleName + "\n"
					is SupervisedHebbianLearning -> result += SupervisedHebbianLearning::class.java.simpleName + "\n"
					is UnsupervisedHebbianLearning -> result += UnsupervisedHebbianLearning::class.java.simpleName + "\n"
					else -> result += "Unknown" + "\n"
				}
				result += "Transfer Function: "
				when (network?.outputNeurons!![0].transferFunction) {
					is Gaussian -> result += Gaussian::class.java.simpleName + "\n"
					is Linear -> result += Linear::class.java.simpleName + "\n"
					is Log -> result += Log::class.java.simpleName + "\n"
					is Ramp -> result += Ramp::class.java.simpleName + "\n"
					is Sgn -> result += Sgn::class.java.simpleName + "\n"
					is Sigmoid -> result += Sigmoid::class.java.simpleName + "\n"
					is Sin -> result += Sin::class.java.simpleName + "\n"
					is Step -> result += Step::class.java.simpleName + "\n"
					is Tanh -> result += Tanh::class.java.simpleName + "\n"
					is Trapezoid -> result += Trapezoid::class.java.simpleName + "\n"
					else -> result += "Unknown" + "\n"
				}
				result += "Layers: "
				for (i in 0..network?.layers?.size!! - 1) {
					if (i == network?.layers?.size!! - 1)
						result += (network?.layers!![i]?.neuronsCount!! - 1).toString()
					else
						result += (network?.layers!![i]?.neuronsCount!! - 1).toString() + ","
				}
				return result
			} else {
				throw FileNotFoundException("File: ${p0?.path} Does Not Exist")
			}
		} catch (ioe: IOException) {
			ioe.printStackTrace(System.err)
			Messages.showErrorDialog(InitialisationAction.project, "${ioe.message}", "Error")
			return ""
		} catch (fnfe: FileNotFoundException) {
			fnfe.printStackTrace(System.err)
			Messages.showErrorDialog(InitialisationAction.project, "${fnfe.message}", "Error")
			return ""
		}
	}
}