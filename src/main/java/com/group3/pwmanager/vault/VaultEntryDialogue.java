package com.group3.pwmanager.vault;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VaultEntryDialogue extends JDialog {
    private JPanel pnl_main;
    private JTextField txt_title;
    private JTextField txt_username;
    private JPasswordField pwd_password;
    private JButton btn_togglePasswordVisibility;
    private JTextArea txa_note;
    private JButton btn_save;
    private JButton btn_saveClose;

    private final char echoChar = pwd_password.getEchoChar();
    private Vault vault;
    private VaultEntry entry;

    public VaultEntryDialogue (Vault vault) {
        // Set up dialogue
        super(vault.getFrame(), ModalityType.APPLICATION_MODAL);
        setTitle("Customize Vault Entry");
        setContentPane(pnl_main);
        pack();
        setLocationRelativeTo(vault.getFrame());
        getRootPane().setDefaultButton(btn_saveClose);

        // Handle dialogue closing
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e) {
                onCancel();
            }
        });

        // Set up buttons
        btn_save.setEnabled(false);
        btn_save.addActionListener(event -> save());
        btn_saveClose.addActionListener(event -> {
            if (save()) dispose();
        });

        btn_togglePasswordVisibility.addActionListener(
            event -> pwd_password.setEchoChar(pwd_password.getEchoChar() == echoChar ? (char) 0 : echoChar));

        // Set keybinds
        pnl_main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        pnl_main.getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                onCancel();
            }
        });

        // Set instance variables
        this.vault = vault;
    }

    public VaultEntryDialogue (Vault vault, VaultEntry entry) {
        this(vault);
        this.entry = entry;

        btn_save.setEnabled(true);

        txt_title.setText(entry.getTitle());
        txt_username.setText(entry.getUsername());
        pwd_password.setText(entry.getPassword());
        txa_note.setText(entry.getNote());
    }

    private boolean save () {
        String title, username, password, note;
        if ((title = txt_title.getText()).isBlank()) {
            JOptionPane.showMessageDialog(this, "Title cannot be empty!");
            return false;
        }
        if ((username = txt_username.getText()).isBlank()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty!");
            return false;
        }
        if ((password = new String(pwd_password.getPassword())).isBlank()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty!");
            return false;
        }
        note = txa_note.getText();

        if (entry != null) {
            entry.setTitle(title);
            entry.setUsername(username);
            entry.setPassword(password);
            entry.setNote(note);
            vault.getTableModel().updateEntry(entry);
        }
        else vault.add(new VaultEntry(title, username, password, note));
        vault.setUnsavedChanges(true);
        return true;
    }

    private void onCancel () {
        dispose();
    }
}