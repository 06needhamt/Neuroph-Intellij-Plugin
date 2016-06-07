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
package com.thomas.needham.neurophidea.project

import com.intellij.facet.FacetManager
import com.intellij.facet.FacetType
import com.intellij.facet.FacetTypeRegistry
import com.intellij.ide.util.projectWizard.JavaModuleBuilder
import com.intellij.ide.util.projectWizard.JavaSettingsStep
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleBuilderListener
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.SettingsStep
import com.intellij.ide.util.projectWizard.SourcePathsBuilder
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.util.projectWizard.WizardInputField
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleType
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.ProjectJdkTable
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.roots.ContentEntry
import com.intellij.openapi.roots.ModifiableRootModel
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ModuleRootModel
import com.intellij.openapi.roots.ui.configuration.ModulesProvider
import com.intellij.openapi.util.Pair
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.thomas.needham.neurophidea.project.facet.NeurophFacet
import com.thomas.needham.neurophidea.project.facet.NeurophFacetConfiguration
import com.thomas.needham.neurophidea.project.facet.NeurophFacetType
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.io.File
import java.util.*
import javax.swing.Icon
import javax.swing.JComponent

/**
 * Created by thoma on 06/06/2016.
 */
class NeurophModuleBuilder : JavaModuleBuilder, ModuleBuilderListener {
    companion object Data{
        var project : Project? = null
        var bundle : ResourceBundle? = null
        var sdk : Sdk? = null
        @JvmStatic fun findContentEntry(@NotNull model: ModuleRootModel?, @NotNull file: VirtualFile?) : ContentEntry?{
            val entries : Array<ContentEntry?> = model?.contentEntries!!
            for(entry : ContentEntry? in entries){
                val entryFile : VirtualFile? = entry?.file
                if(entryFile != null && VfsUtilCore.isAncestor(entryFile!!,file!!,false)){
                    return entry
                }
            }
            return null
        }

        @JvmStatic fun setupFacet(module: Module?, sdk: Sdk?){
            val facetId = NeurophFacetType.getInstance()?.stringId
            if(!StringUtil.isEmptyOrSpaces(facetId)){
                val facetManager = FacetManager.getInstance(module!!)
                val facetType = FacetTypeRegistry.getInstance().findFacetType(facetId)
                if(facetType != null){
                    if(facetManager.getFacetByType(facetType.id) != null){
                        var model = facetManager.createModifiableModel()
                        var facet : NeurophFacet? = facetManager.addFacet(facetType, facetType.defaultFacetName,null) as NeurophFacet?
                        facet?.configuration?.setSdk(sdk)
                        model.addFacet(facet)
                        model.commit()
                    }
                }
            }
        }
    }
    constructor(){
        addListener(this)
    }

    override fun moduleCreated(p0 : Module) {
        if(sdk != null && sdk?.sdkType is NeurophSDK){
            setupFacet(p0,sdk)
            val roots : Array<VirtualFile?> = ModuleRootManager.getInstance(p0).getSourceRoots()
            if(roots.size == 1){
                val sourceRoot : VirtualFile? = roots[0]
                if(sourceRoot?.name?.equals("Neuroph")!!){
                    val main = sourceRoot?.parent
                    if(main != null && "main".equals(main.name)){
                        val src : VirtualFile? = main.parent
                        if(src != null){
                            ApplicationManager.getApplication().runWriteAction(RunWriteActionRunnable(this,src,p0))
                        }
                    }
                }
            }
        }
    }

    override fun getBuilderId() : String? {
        return "neuroph"
    }

    override fun getBigIcon() : Icon? {
        return super.getBigIcon()
    }

    override fun getNodeIcon() : Icon? {
        return super.getNodeIcon()
    }

    override fun getDescription() : String? {
        if (ProjectJdkTable.getInstance().getSdksOfType(NeurophSDK.getInstance()).isEmpty()) {
            return "<html><body>Before you start make sure you have Neuroph installed." +
                    "<br/>Download <a href='http://downloads.sourceforge.net/project/neuroph/neuroph-2.92/neurophstudio-2.92.zip?r=http%3A%2F%2Fneuroph.sourceforge.net%2Fdownload.html&ts=1465207712&use_mirror=tenet'>the latest version</a>" +
                    "<br/>Unpack the zip file to any folder and select it as Redline SDK</body></html>";
        }
        else {
            return "Neuroph Module"
        }
    }

    override fun getPresentableName() : String? {
        return "Neuroph Module"
    }

    override fun getGroupName() : String? {
        return "Neuroph"
    }

    override fun createWizardSteps(wizardContext : WizardContext, modulesProvider : ModulesProvider) : Array<out ModuleWizardStep>? {
        val step = NeurophWizardStep(this)
        return arrayOf(step)
    }

    fun setSdk(s: Sdk?) : Unit {
        sdk = s;
    }

    @Nullable
    override fun modifySettingsStep(settingsStep : SettingsStep) : ModuleWizardStep? {
        var settingsStep : JavaSettingsStep? = super.modifySettingsStep(settingsStep) as JavaSettingsStep?
        if(settingsStep != null){
            settingsStep.setSourcePath("src${File.separator}main${File.separator}neuroph")
        }
        return settingsStep
    }

    override fun getAdditionalFields() : MutableList<WizardInputField<JComponent>>? {
        val field = NeurophWizardInputField()
        return listOf<WizardInputField<*>>(field) as MutableList<WizardInputField<JComponent>>
    }
}