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
package com.thomas.needham.neurophidea.designer.psi.snnet.compiler.generator

import com.intellij.openapi.ui.Messages
import com.thomas.needham.neurophidea.Tuple3
import com.thomas.needham.neurophidea.actions.CompileSnnetFileAction
import com.thomas.needham.neurophidea.datastructures.LearningRules
import com.thomas.needham.neurophidea.datastructures.NetworkConfiguration
import com.thomas.needham.neurophidea.datastructures.NetworkTypes
import com.thomas.needham.neurophidea.datastructures.TransferFunctions
import com.thomas.needham.neurophidea.designer.psi.snnet.compiler.parser.SnnetProperties
import java.io.*

/**
 * Created by thoma on 01/07/2016.
 */
class SnnetGenerator {
	val networkDesc: SnnetProperties<Any>?
	var networkConf: NetworkConfiguration?
	val ToIntArray: (Array<Double>) -> Array<Int> = { e ->
		val arr = IntArray(e.size)
		for (i in 0..e.size - 1) {
			arr[i] = e[i].toInt()
		}
		arr.toTypedArray()
	}
	
	constructor(networkDesc: SnnetProperties<Any>) {
		this.networkDesc = networkDesc
		this.networkConf = GenerateNetwork()
	}
	
	private fun GenerateNetwork(): NetworkConfiguration? {
		var name: String = ""
		var type: NetworkTypes.Types = NetworkTypes.Types.values()[0]
		var rule: LearningRules.Rules = LearningRules.Rules.values()[0]
		var function: TransferFunctions.Functions = TransferFunctions.Functions.values()[0]
		var layers: Array<Double>? = null
		for (prop: Tuple3<*, *, *> in networkDesc?.properties!!) {
			when (prop.valueY as String) {
				"NetworkName" -> name = prop.valueZ as String
				"NetworkType" -> type = { NetworkTypes.Types.values()[NetworkTypes.classNames.indexOf(prop.valueZ as String)] }()
				"NetworkLearningRule" -> rule = { LearningRules.Rules.values()[LearningRules.classNames.indexOf(prop.valueZ as String)] }()
				"NetworkTransferFunction" -> function = { TransferFunctions.Functions.values()[TransferFunctions.classNames.indexOf(prop.valueZ as String)] }()
				"NetworkLayers" -> layers = prop.valueZ as Array<Double>?
			}
		}
		return NetworkConfiguration(name, type, ToIntArray(layers!!), rule, function, "", "", "${CompileSnnetFileAction.projectDirectory}")
	}
	
	fun SaveNetwork(path: String) {
		val file = File(path)
		try {
			val fos = FileOutputStream(file)
			val oos = ObjectOutputStream(fos)
			oos.writeObject(networkConf)
			oos.close()
		} catch (ioe: IOException) {
			ioe.printStackTrace(System.err)
			Messages.showErrorDialog(CompileSnnetFileAction.project, "Error Writing Compiled Network To File", "Error")
			return
		} catch (fnfe: FileNotFoundException) {
			fnfe.printStackTrace(System.err)
			Messages.showErrorDialog(CompileSnnetFileAction.project, "File Does Not Exist ${file.path}", "Error")
			return
		}
	}
}