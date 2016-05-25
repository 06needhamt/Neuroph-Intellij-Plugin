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
