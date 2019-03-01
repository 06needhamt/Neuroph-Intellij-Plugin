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
package com.thomas.needham.neurophidea.project.facet

import com.intellij.facet.Facet
import com.intellij.facet.FacetManager
import com.intellij.facet.FacetType
import com.intellij.openapi.module.Module
import org.jetbrains.annotations.NotNull

/**
 * Created by Thomas Needham on 07/06/2016.
 */
class NeurophFacet : Facet<NeurophFacetConfiguration> {
	constructor(@NotNull type: FacetType<NeurophFacet, NeurophFacetConfiguration>?,
				@NotNull module: Module?,
				name: String,
				@NotNull config: NeurophFacetConfiguration,
				underlying: Facet<NeurophFacetConfiguration>) : super(type!!, module!!, name, config, underlying) {
		
	}
	
	companion object Instance {
		@JvmStatic
		fun getInstance(module: Module?): NeurophFacet? {
			return FacetManager.getInstance(module!!).getFacetByType(NeurophFacetType.FACET_TYPE_ID)
		}
	}
}