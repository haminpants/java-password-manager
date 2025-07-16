package com.group3.pwmanager.vaults;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VaultEntryMenu extends JDialog {
    private JPanel pnl_main;
    private JTextField txt_title;
    private JTextField txt_username;
    private JPasswordField pwd_password;
    private JTextArea txa_note;
    private JButton btn_save;

    private static final char visibleEchoChar = (char) 0;
    private final char echoChar;

    private VaultMenu vaultMenu;
    private VaultEntry entry;

    public VaultEntryMenu (VaultMenu vaultMenu) {
        super((JFrame) SwingUtilities.getWindowAncestor(vaultMenu.getContentPane()));
        setTitle("Configure Vault Entry");
        setContentPane(pnl_main);
        setModalityType(ModalityType.DOCUMENT_MODAL);
        getRootPane().setDefaultButton(btn_save);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e) {
                onCancel();
            }
        });

        btn_save.addActionListener(event -> onSave());

        this.echoChar = pwd_password.getEchoChar();
        this.vaultMenu = vaultMenu;
    }

    public void onSave () {
        String title, username, password;
        if ((title = txt_title.getText()).isBlank()) {
            JOptionPane.showMessageDialog(this, "Title cannot be blank.");
            return;
        }
        if ((username = txt_username.getText()).isBlank()) {
            JOptionPane.showMessageDialog(this, "Username cannot be blank.");
            return;
        }
        if ((password = getPassword()).isBlank()) {
            JOptionPane.showMessageDialog(this, "Password cannot be blank.");
            return;
        }

        if (entry != null) {
            entry.setTitle(title);
            entry.setUsername(username);
            entry.setPassword(password);
            entry.setNote(txa_note.getText());
        }
        else {
            entry = new VaultEntry(title, username, getPassword(), txa_note.getText());
            vaultMenu.getVault().addEntry(entry);
            vaultMenu.refreshTable();
        }

        dispose();
    }

    public void onCancel () {
        dispose();
    }

    private String getPassword () {
        StringBuilder s = new StringBuilder();
        for (char c : pwd_password.getPassword()) s.append(c);
        return s.toString();
    }
}