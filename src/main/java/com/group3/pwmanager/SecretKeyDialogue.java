package com.group3.pwmanager;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class SecretKeyDialogue extends JDialog {
    private JPanel pnl_main;
    private JLabel lbl_prompt;
    private JPasswordField pwd_key;
    private JButton btn_toggleVisibility;
    private JButton btn_submit;

    private final char echoChar = pwd_key.getEchoChar();
    private SecretKey key;

    public SecretKeyDialogue (Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setContentPane(pnl_main);
        getRootPane().setDefaultButton(btn_submit);

        // Set up buttons
        btn_submit.addActionListener(event -> onSubmit());

        btn_toggleVisibility.addActionListener(
            event -> pwd_key.setEchoChar(pwd_key.getEchoChar() == echoChar ? (char) 0 : echoChar));

        // Set keybinds
        pnl_main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        pnl_main.getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                onCancel();
            }
        });
    }

    public SecretKey showKeyDialogue () {
        lbl_prompt.setText("Enter the key for your vault");

        pack();
        setLocationRelativeTo(getOwner());
        setVisible(true);
        return key;
    }

    private void onSubmit () {
        String keyString = new String(pwd_key.getPassword());
        if (keyString.isBlank()) return;

        key = EncryptionUtils.generateKeyFromString(keyString);
        dispose();
    }

    private void onCancel () {
        dispose();
    }
}