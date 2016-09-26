/*
    The MIT License (MIT)
    
    neuroph-plugin Copyright (c) 2016 thoma
    
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

package com.thomas.needham.neurophidea.settings

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.options.Configurable
import java.awt.event.ItemEvent
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JPanel
import com.thomas.needham.neurophidea.Constants.GENERATE_CODE_KEY
import com.thomas.needham.neurophidea.Constants.GENERATE_JAVA_KEY
import com.thomas.needham.neurophidea.Constants.GENERATE_GROOVY_KEY
import com.thomas.needham.neurophidea.Constants.GENERATE_SCALA_KEY
import com.thomas.needham.neurophidea.Constants.GENERATE_KOTLIN_KEY

/**
 * Created by thoma on 13/09/2016.
 */
class AutoGenerateCodeSetting : Configurable {
    var generate : Boolean = false
    var generateJava : Boolean = false
    var generateGroovy : Boolean = false
    var generateScala : Boolean = false
    var generateKotlin : Boolean = false
    var properties : PropertiesComponent = PropertiesComponent.getInstance()

    companion object Interface {
        var modified : Boolean = true
        val panel : JPanel = JPanel(null)
        val insets = panel.insets
        val generateCheckBox : JCheckBox = JCheckBox("Regenerate Code On Change")
        val javaCheckBox : JCheckBox = JCheckBox("Generate Java Code")
        val groovyCheckBox: JCheckBox = JCheckBox("Generate Groovy Code")
        val scalaCheckBox : JCheckBox = JCheckBox("Generate Scala Code")
        val kotlinCheckBox : JCheckBox = JCheckBox("Generate Kotlin Code")
        //val checkBoxGroupName : String = "CodeGenerationGroup"
    }

    override fun isModified() : Boolean {
        return modified
    }

    override fun getDisplayName() : String {
        return "Code Generator Settings"
    }

    override fun apply() {
        properties.setValue(GENERATE_CODE_KEY, generate)
        properties.setValue(GENERATE_JAVA_KEY, generateJava)
        properties.setValue(GENERATE_GROOVY_KEY, generateGroovy)
        properties.setValue(GENERATE_SCALA_KEY, generateScala)
        properties.setValue(GENERATE_KOTLIN_KEY, generateKotlin)
    }

    override fun createComponent() : JComponent {
        generateCheckBox.setBounds(insets.left + 50, insets.top + 30, 100, 25)
        panel.add(generateCheckBox)
        javaCheckBox.setBounds(generateCheckBox.bounds.x + 50, generateCheckBox.bounds.y + 30, 200, 25)
        panel.add(javaCheckBox)
        groovyCheckBox.setBounds(javaCheckBox.bounds.x, javaCheckBox.bounds.y + 30, 200, 25)
        panel.add(groovyCheckBox)
        scalaCheckBox.setBounds(groovyCheckBox.bounds.x, groovyCheckBox.bounds.y + 30, 200, 25)
        panel.add(scalaCheckBox)
        kotlinCheckBox.setBounds(scalaCheckBox.bounds.x, scalaCheckBox.bounds.y + 30, 200, 25)
        panel.add(kotlinCheckBox)
        generateCheckBox.addItemListener { e ->
            modified = true
            if (e.stateChange.and(ItemEvent.SELECTED) != 0) {
                generate = true
                javaCheckBox.isEnabled = true
                groovyCheckBox.isEnabled = true
                scalaCheckBox.isEnabled = true
                kotlinCheckBox.isEnabled = true
            } else if (e.stateChange.and(ItemEvent.DESELECTED) != 0) {
                generate = false
                generateJava = false
                generateGroovy = false
                generateScala = false
                generateKotlin = false
                javaCheckBox.isEnabled = false
                groovyCheckBox.isEnabled = false
                scalaCheckBox.isEnabled = false
                kotlinCheckBox.isEnabled = false
            }
        }
        javaCheckBox.addItemListener { e ->
            modified = true
            if (e.stateChange.and(ItemEvent.SELECTED) != 0) {
                if (generate)
                    generateJava = true
            } else if (e.stateChange.and(ItemEvent.DESELECTED) != 0) {
                if (generate)
                    generateJava = false
            }
        }
        groovyCheckBox.addItemListener { e ->
            modified = true
            if (e.stateChange.and(ItemEvent.SELECTED) != 0) {
                if (generate)
                    generateGroovy = true
            } else if (e.stateChange.and(ItemEvent.DESELECTED) != 0) {
                if (generate)
                    generateGroovy = false
            }
        }
        scalaCheckBox.addItemListener { e ->
            modified = true
            if (e.stateChange.and(ItemEvent.SELECTED) != 0) {
                if (generate)
                    generateScala = true
            } else if (e.stateChange.and(ItemEvent.DESELECTED) != 0) {
                if (generate)
                    generateScala = false
            }
        }
        kotlinCheckBox.addItemListener { e ->
            modified = true
            if (e.stateChange.and(ItemEvent.SELECTED) != 0) {
                if (generate)
                    generateKotlin = true
            } else if (e.stateChange.and(ItemEvent.DESELECTED) != 0) {
                if (generate)
                    generateKotlin = false
            }
        }
        return panel
    }

    override fun reset() {
        modified = false
        generate = properties.getBoolean(GENERATE_CODE_KEY, false)
        generateJava = properties.getBoolean(GENERATE_JAVA_KEY, false)
        generateGroovy = properties.getBoolean(GENERATE_GROOVY_KEY, false)
        generateScala = properties.getBoolean(GENERATE_SCALA_KEY, false)
        generateKotlin = properties.getBoolean(GENERATE_KOTLIN_KEY, false)
    }

    override fun getHelpTopic() : String {
        return ""
    }
}