package com.group3.pwmanager;

import com.google.gson.Gson;
import com.group3.pwmanager.vaults.Vault;
import com.group3.pwmanager.vaults.VaultMenu;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;

public class HomeMenu implements Menu {
    private JPanel contentPane;
    private JButton btn_newVault;
    private JButton btn_loadVault;
    private JList<String> lst_recentVaults;

    private final JFileChooser fileChooser = new JFileChooser();

    public HomeMenu () {
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public String getDescription () {
                return "Java Password Manager Vault (*.jpmv)";
            }

            @Override
            public boolean accept (File file) {
                return file.getName().toLowerCase().endsWith(".jpmv");
            }
        });

        btn_newVault.addActionListener(event -> Main.setAppContent(new VaultMenu(new Vault("New Vault"))));

        btn_loadVault.addActionListener(event -> {
            if (fileChooser.showOpenDialog(contentPane) != JFileChooser.APPROVE_OPTION) return;
            try (FileReader fileReader = new FileReader(fileChooser.getSelectedFile());
                BufferedReader reader = new BufferedReader(fileReader)) {
                StringBuilder contents = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    contents.append(line);
                }
                Vault vault = Main.GSON.fromJson(contents.toString(), Vault.class);
                Main.setAppContent(new VaultMenu(vault));
            }
            catch (FileNotFoundException e) {
                throw new RuntimeException(e);
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
    public Dimension getPreferredSize () {
        return new Dimension(300, 400);
    }

    @Override
    public JMenuBar getMenuBar () {
        return null;
    }
}