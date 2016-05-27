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
package com.thomas.needham.neurophidea.datastructures

/**
 * Created by thoma on 24/05/2016.
 */
class NeurophVersions {
    enum class Versions(val version : String) {
        //Enum must be nested within a class to avoid a NoClassDefFoundException possible Kotlin bug???
        VERSION_2_9("2.9"),
        VERSION_2_8("2.8"),
        VERSION_2_7("2.7"),
        VERSION_2_6("2.6"),
        VERSION_2_5RC2("2.5RC2"),
        VERSION_2_5RC1("2.5RC1"),
        VERSION_2_5("2.5"),
        VERSION_2_4("2.4"),
        VERSION_2_3_1("2.3.1"),
        VERSION_2_3("2.3"),
        VERSION_2_2("2.2"),
        VERSION_2_1_1_BETA("2.1.1_beta"),
        VERSION_2_1_0_BETA("2.1.0_beta"),
        VERSION_2_0_0_ALPHA("2.0.0_alpha"),
        VERSION_1_0_1_ALPHA("1.0.1_alpha"),
        VERSION_1_0_0_ALPHA("1.0.0_alpha");
    }
    companion object Links{
        val URLS = arrayOf("http://netix.dl.sourceforge.net/project/neuroph/neuroph-2.9/neuroph-2.9.zip",
                "http://netix.dl.sourceforge.net/project/neuroph/neuroph-2.8/neuroph-2.8.zip",
                "http://netix.dl.sourceforge.net/project/neuroph/neuroph-2.7/neuroph-2.7.zip",
                "http://netix.dl.sourceforge.net/project/neuroph/neuroph-2.6/neuroph-2.6.zip",
                "http://netix.dl.sourceforge.net/project/neuroph/neuroph-2.5.1RC2/neuroph-2.5.1RC2.zip",
                "http://netix.dl.sourceforge.net/project/neuroph/neuroph-2.5.1/neuroph-2.5.1.zip",
                "http://netix.dl.sourceforge.net/project/neuroph/neuroph-2.5/neuroph-2.5.zip")
    }
}