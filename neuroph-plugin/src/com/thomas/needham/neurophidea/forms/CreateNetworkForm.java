package com.thomas.needham.neurophidea.forms;

import javax.swing.*;
import java.awt.*;

/**
 * Created by thoma on 25/05/2016.
 */
public class CreateNetworkForm extends JFrame{
    private JPanel inner;
    private JComboBox cmbNetworkType;
    private JTextField txtNetworkName;
    private JButton btnSaveNetwork;
    private JTextField txtLayers;
    private JComboBox cmbLearningRule;
    private JComboBox cmbTransferFunction;
    private JTextField txtTrainingData;
    private JButton btnBrowseTrainingData;
    private JTextField txtNetworkOutputPath;
    private JButton btnBrowseOutput;
    private JPanel root;

    public CreateNetworkForm() throws HeadlessException {
        super("Hello");
        this.setContentPane(root);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    private void CreateMenu(){
        
    }
}
