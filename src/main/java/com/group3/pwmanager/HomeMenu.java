package com.group3.pwmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.pwmanager.vault.Vault;
import com.group3.pwmanager.vault.VaultBuilder;
import com.group3.pwmanager.vault.VaultEntry;
import com.group3.pwmanager.vault.VaultEntryTableModel;
import com.group3.pwmanager.vault.adapters.VaultAdapter;
import com.group3.pwmanager.vault.adapters.VaultBuilderAdapter;
import com.group3.pwmanager.vault.adapters.VaultEntryAdapter;
import com.group3.pwmanager.vault.adapters.VaultEntryTableModelAdapter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class HomeMenu {
    private JPanel pnl_main;
    private JButton btn_createVault;
    private JButton btn_loadVault;
    private JList<String> lst_recentVaults;

    private final JFrame frame = new JFrame();
    private final Gson gson;
    private final JFileChooser fileChooser = new JFileChooser();

    public HomeMenu () {
        // Gson setup
        gson = new GsonBuilder().registerTypeAdapter(Vault.class, new VaultAdapter())
            .registerTypeAdapter(VaultBuilder.class, new VaultBuilderAdapter())
            .registerTypeAdapter(VaultEntry.class, new VaultEntryAdapter())
            .registerTypeAdapter(VaultEntryTableModel.class, new VaultEntryTableModelAdapter()).create();

        // File chooser setup
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Java Password Manager Vault (*." + Vault.FILE_EXTENSION + ")", Vault.FILE_EXTENSION));

        // Setup JFrame
        frame.setTitle("Start Menu - Java Password Manager");
        frame.setContentPane(pnl_main);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Setup buttons
        btn_createVault.addActionListener(event -> {
            Vault vault = new Vault(this);
            vault.setVisible(true);
            setVisible(false);
        });

        btn_loadVault.addActionListener(event -> {
            if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) return;
            try (FileReader reader = new FileReader(fileChooser.getSelectedFile())) {
                VaultBuilder vaultBuilder = gson.fromJson(reader, VaultBuilder.class);
                vaultBuilder.setFile(fileChooser.getSelectedFile());

                Vault vault = vaultBuilder.build(this);
                vault.setVisible(true);
                setVisible(false);
            }
            catch (FileNotFoundException e) {
                // TODO: implement robust handling
                throw new RuntimeException(e);
            }
            catch (IOException e) {
                // TODO: implement robust handling
                throw new RuntimeException(e);
            }
        });
    }

    public void setVisible (boolean visible) {
        frame.setVisible(visible);
    }

    public Gson getGson () {
        return gson;
    }

    public JFileChooser getFileChooser () {
        return fileChooser;
    }
}