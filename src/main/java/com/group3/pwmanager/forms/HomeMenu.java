package com.group3.pwmanager.forms;

import javax.swing.*;
import java.awt.*;

public class HomeMenu {
    public static final Dimension PREFERRED_SIZE = new Dimension(300, 400);

    private JPanel contentPane;
    private JButton btn_newVault;
    private JButton btn_loadVault;
    private JList lst_recentVaults;

    public HomeMenu(JFrame parent) {
        btn_newVault.addActionListener(event -> {
            parent.setContentPane(new VaultMenu().getContentPane());
            parent.revalidate();
            parent.setPreferredSize(VaultMenu.PREFERRED_SIZE);
            parent.setSize(VaultMenu.PREFERRED_SIZE);
            parent.setLocationRelativeTo(null);
        });
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}