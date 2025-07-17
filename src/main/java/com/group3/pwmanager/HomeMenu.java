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

import javax.crypto.AEADBadTagException;
import javax.crypto.SecretKey;
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
            .registerTypeAdapter(VaultEntryTableModel.class, new VaultEntryTableModelAdapter())
            .create();

        // File chooser setup
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(
            new FileNameExtensionFilter("Java Password Manager Vault (*." + Vault.FILE_EXTENSION + ")",
                Vault.FILE_EXTENSION));

        // Setup JFrame
        frame.setTitle("Start Menu - Java Password Manager");
        frame.setContentPane(pnl_main);
        frame.pack();

        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Setup buttons
        btn_createVault.addActionListener(event -> {
            SecretKey key = new SecretKeyDialogue(frame).showKeyDialogue();
            if (key == null) return;

            Vault vault = new Vault(this, key);
            vault.setVisible(true);
            setVisible(false);
        });

        btn_loadVault.addActionListener(event -> {
            if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) return;

            StringBuilder cipherText = new StringBuilder();
            try (FileReader reader = new FileReader(fileChooser.getSelectedFile())) {
                int c;
                while ((c = reader.read()) != -1) cipherText.append((char) c);
            }
            catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(frame, "Selected file does not exist", "Oops!",
                    JOptionPane.ERROR_MESSAGE);
            }
            catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Unexpected error occurred", "Oops!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

            SecretKey key = new SecretKeyDialogue(frame).showKeyDialogue();
            if (key == null) return;

            try {
                String vaultJson = EncryptionUtils.decrypt(cipherText.toString(), key);
                VaultBuilder vaultBuilder = gson.fromJson(vaultJson, VaultBuilder.class)
                    .setFile(fileChooser.getSelectedFile());

                Vault vault = vaultBuilder.build(this, key);
                vault.setVisible(true);
                setVisible(false);
            }
            catch (AEADBadTagException e) {
                JOptionPane.showMessageDialog(frame, "Entered key is incorrect", "Invalid Key",
                    JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Unexpected error occurred", "Oops!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    public Gson getGson () {
        return gson;
    }

    public JFileChooser getFileChooser () {
        return fileChooser;
    }

    public void setVisible (boolean visible) {
        frame.setVisible(visible);
    }
}