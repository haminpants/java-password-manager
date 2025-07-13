package com.group3.pwmanager;

import com.group3.pwmanager.vaults.Vault;
import com.group3.pwmanager.vaults.VaultMenu;
import com.group3.pwmanager.vaults.swingutils.VaultFileFilter;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class HomeMenu implements Menu {
    private JPanel contentPane;
    private JButton btn_newVault;
    private JButton btn_loadVault;
    private JList<String> lst_recentVaults;

    private final JFileChooser fileChooser = new JFileChooser();

    public HomeMenu () {
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new VaultFileFilter());

        btn_newVault.addActionListener(event -> Main.setAppContent(new VaultMenu()));

        btn_loadVault.addActionListener(event -> {
            if (fileChooser.showOpenDialog(contentPane) != JFileChooser.APPROVE_OPTION) return;
            try {
                Main.setAppContent(new VaultMenu(Vault.fromFile(fileChooser.getSelectedFile())));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public JPanel getContentPane () {
        return contentPane;
    }

    @Override
    public String getTitle () {
        return "Java Password Manager";
    }

    @Override
    public JMenuBar getMenuBar () {
        return null;
    }
}