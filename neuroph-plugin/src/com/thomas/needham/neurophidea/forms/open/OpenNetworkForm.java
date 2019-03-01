package com.thomas.needham.neurophidea.forms.open;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.neuroph.core.NeuralNetwork;

import javax.swing.*;
import java.awt.*;

/**
 * Created by thoma on 13/06/2016.
 */
public class OpenNetworkForm extends JFrame {
    public JPanel root;
    public JPanel inner;
    public JTextField txtNetworkToOpen;
    public JButton btnBrowseNetwork;
    public JButton btnOpenNetwork;
    public NeuralNetwork network;
    public boolean shouldClose = false;

    public OpenNetworkForm() throws HeadlessException {
        super("Open Existing Neural Network");
        this.setContentPane(root);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Toolkit t = Toolkit.getDefaultToolkit();
        int x = (int) ((t.getScreenSize().getWidth() - (double) this.getWidth()) / 2.0D);
        int y = (int) ((t.getScreenSize().getHeight() - (double) this.getHeight()) / 2.0D);
        this.setLocation(x, y);
        OpenNetworkFormImplementationKt.AddOnClickListeners(this);
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
        inner.setLayout(new GridLayoutManager(2, 3, new Insets(5, 10, 0, 5), -1, -1));
        root.add(inner, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        label1.setText("Network To Open");
        inner.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        txtNetworkToOpen = new JTextField();
        inner.add(txtNetworkToOpen, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        btnBrowseNetwork = new JButton();
        btnBrowseNetwork.setText("Browse");
        inner.add(btnBrowseNetwork, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btnOpenNetwork = new JButton();
        btnOpenNetwork.setText("Open Network");
        inner.add(btnOpenNetwork, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return root;
    }

}
