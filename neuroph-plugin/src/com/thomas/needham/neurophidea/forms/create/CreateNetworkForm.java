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
package com.thomas.needham.neurophidea.forms.create;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Thomas Needham on 25/05/2016.
 */
public class CreateNetworkForm extends JFrame {
    public JPanel inner;
    public JComboBox<String> cmbNetworkType;
    public JTextField txtNetworkName;
    public JButton btnSaveNetwork;
    public JTextField txtLayers;
    public JComboBox<String> cmbLearningRule;
    public JComboBox<String> cmbTransferFunction;
    public JTextField txtTrainingData;
    public JButton btnBrowseTrainingData;
    public JTextField txtNetworkOutputPath;
    public JButton btnBrowseOutput;
    public JPanel root;
    public JTextField txtTestingData;
    public JButton btnBrowseTestingData;

    public CreateNetworkForm() throws HeadlessException {
        super("Create New Neural Network");
        this.setContentPane(root);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Toolkit t = Toolkit.getDefaultToolkit();
        int x = (int) ((t.getScreenSize().getWidth() - (double) this.getWidth()) / 2.0D);
        int y = (int) ((t.getScreenSize().getHeight() - (double) this.getHeight()) / 2.0D);
        this.setLocation(x, y);
        CreateNetworkFormImplementationKt.PopulateNetworktypes(this);
        CreateNetworkFormImplementationKt.PopulateLearningRules(this);
        CreateNetworkFormImplementationKt.PopulateTransferFunctions(this);
        CreateNetworkFormImplementationKt.AddOnClickListeners(this);
        this.pack();
        this.setVisible(true);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        root = new JPanel();
        root.setLayout(new BorderLayout(0, 0));
        inner = new JPanel();
        inner.setLayout(new GridLayoutManager(9, 3, new Insets(5, 10, 0, 5), -1, -1));
        inner.setName("root");
        root.add(inner, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        label1.setText("Network Name");
        inner.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Training Data");
        inner.add(label2, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Transfer Function");
        inner.add(label3, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Learning Rule");
        inner.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Network Type");
        inner.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Layers");
        inner.add(label6, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Network Output Path");
        inner.add(label7, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbNetworkType = new JComboBox();
        cmbNetworkType.setName("cmbNetworkType");
        inner.add(cmbNetworkType, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNetworkName = new JTextField();
        txtNetworkName.setName("txtNetworkName");
        txtNetworkName.setPreferredSize(new Dimension(100, 24));
        inner.add(txtNetworkName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnSaveNetwork = new JButton();
        btnSaveNetwork.setLabel("Save Network");
        btnSaveNetwork.setName("btnSaveNetwork");
        btnSaveNetwork.setText("Save Network");
        inner.add(btnSaveNetwork, new GridConstraints(8, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtLayers = new JTextField();
        txtLayers.setName("txtLayers");
        inner.add(txtLayers, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cmbLearningRule = new JComboBox();
        cmbLearningRule.setName("cmbLearningRule");
        inner.add(cmbLearningRule, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cmbTransferFunction = new JComboBox();
        cmbTransferFunction.setName("cmbTransferFunction");
        inner.add(cmbTransferFunction, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTrainingData = new JTextField();
        txtTrainingData.setName("txtTrainingData");
        txtTrainingData.setText("");
        inner.add(txtTrainingData, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnBrowseTrainingData = new JButton();
        btnBrowseTrainingData.setHorizontalAlignment(4);
        btnBrowseTrainingData.setLabel("Browse");
        btnBrowseTrainingData.setName("btnBrowse");
        btnBrowseTrainingData.setText("Browse");
        inner.add(btnBrowseTrainingData, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNetworkOutputPath = new JTextField();
        txtNetworkOutputPath.setName("txtNetworkOutputPath");
        inner.add(txtNetworkOutputPath, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnBrowseOutput = new JButton();
        btnBrowseOutput.setText("Browse");
        inner.add(btnBrowseOutput, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Testing Data");
        inner.add(label8, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtTestingData = new JTextField();
        inner.add(txtTestingData, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnBrowseTestingData = new JButton();
        btnBrowseTestingData.setText("Browse");
        inner.add(btnBrowseTestingData, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

}
