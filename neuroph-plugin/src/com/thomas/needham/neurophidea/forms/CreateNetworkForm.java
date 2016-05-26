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
package com.thomas.needham.neurophidea.forms;

import com.thomas.needham.neurophidea.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by thoma on 25/05/2016.
 */
public class CreateNetworkForm extends JFrame{
    private JPanel inner;
    private JComboBox<String> cmbNetworkType;
    private JTextField txtNetworkName;
    private JButton btnSaveNetwork;
    private JTextField txtLayers;
    private JComboBox<String> cmbLearningRule;
    private JComboBox<String> cmbTransferFunction;
    private JTextField txtTrainingData;
    private JButton btnBrowseTrainingData;
    private JTextField txtNetworkOutputPath;
    private JButton btnBrowseOutput;
    private JPanel root;

    public CreateNetworkForm() throws HeadlessException {
        super("Create New Neural Network");
        this.setContentPane(root);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Toolkit t = Toolkit.getDefaultToolkit();
        int x = (int) ((t.getScreenSize().getWidth() - (double) this.getWidth()) / 2.0D);
        int y = (int) ((t.getScreenSize().getHeight() - (double) this.getHeight()) / 2.0D);
        this.setLocation(x,y);
        PopulateNetworktypes();
        PopulateLearningRules();
        PopulateTransferFunctions();
        this.pack();
        this.setVisible(true);
    }

    private void PopulateTransferFunctions() {
        for(int i = 0; i < TransferFunctions.Functions.values().length; i++){
            cmbTransferFunction.addItem(TransferFunctions.getFriendlyNames()[i]);
        }
    }

    private void PopulateNetworktypes() {
        for(int i = 0; i < NetworkTypes.Types.values().length; i++){
            cmbNetworkType.addItem(NetworkTypes.getFriendlyNames()[i]);
        }
    }

    private void PopulateLearningRules() {
        for(int i = 0; i < LearningRules.Rules.values().length; i++){
            cmbLearningRule.addItem(LearningRules.getFriendlyNames()[i]);
        }
    }
    private void CreateMenu(){
        
    }
}
