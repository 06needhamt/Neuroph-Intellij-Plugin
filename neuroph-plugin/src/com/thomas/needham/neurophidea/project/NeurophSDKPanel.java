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
package com.thomas.needham.neurophidea.project;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.ui.components.labels.ActionLink;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.thomas.needham.neurophidea.actions.DownloadNeurophAction;
import com.thomas.needham.neurophidea.datastructures.NeurophVersions;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;

/**
 * Created by Thomas Needham on 06/06/2016.
 */
public class NeurophSDKPanel extends JFrame {
    public JPanel root;
    public JPanel inner;
    public NeurophSDKComboBox cmbSDKVersion;
    public ActionLink myDownloadLink;
    public AnAction action;

    public NeurophSDKPanel() {
        super("Select Neuroph SDK");
        $$$setupUI$$$();
        this.setContentPane(root);
        this.pack();
    }

    private void createUIComponents() {
        action = new DownloadNeurophAction();
        myDownloadLink = new ActionLink("Download and Install Neuroph", action);
        cmbSDKVersion = new NeurophSDKComboBox();
//        for (int i = 0; i < NeurophVersions.Versions.values().length; i++) {
//            final NeurophSDK sdk = new NeurophSDK();
//            cmbSDKVersion.getComboBox().addItem(sdk);
//        }
    }

    public String getSdkName() {
        final Sdk selectedSdk = cmbSDKVersion.getSelectedSdk();
        return selectedSdk == null ? null : selectedSdk.getName();
    }

    public Sdk getSdk() {
        return cmbSDKVersion.getSelectedSdk();
    }

    public void setSdk(Sdk sdk) {
        cmbSDKVersion.getComboBox().setSelectedItem(sdk);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        root = new JPanel();
        root.setLayout(new BorderLayout(0, 0));
        inner = new JPanel();
        inner.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        root.add(inner, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        label1.setText("Neuroph SDK");
        inner.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        inner.add(cmbSDKVersion, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }
}
