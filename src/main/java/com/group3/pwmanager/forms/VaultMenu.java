package com.group3.pwmanager.forms;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class VaultMenu {
    public static final Dimension PREFERRED_SIZE = new Dimension(900, 600);

    private JPanel contentPane;
    private JTree tre_entries;
    private DefaultTreeModel entryTreeModel;

    public VaultMenu() {
        entryTreeModel = (DefaultTreeModel) tre_entries.getModel();
        entryTreeModel.setRoot(new DefaultMutableTreeNode("Root"));
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}