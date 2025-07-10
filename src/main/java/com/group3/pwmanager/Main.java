package com.group3.pwmanager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.group3.pwmanager.forms.Menu;
import com.group3.pwmanager.forms.VaultMenu;

import javax.swing.*;

public class Main {
    private static final JFrame appFrame = new JFrame();

    public static void main(String[] args) {
        FlatDarkLaf.setup();

        Vault vault = new Vault("Test Vault");
        VaultMenu menu = new VaultMenu(vault);
        setAppContent(menu);

        appFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        appFrame.setLocationRelativeTo(null);
        appFrame.setVisible(true);
    }

    public static void setAppContent (Menu menu) {
        appFrame.setContentPane(menu.getContentPane());
        appFrame.setPreferredSize(menu.getPreferredSize());
        appFrame.pack();
        appFrame.revalidate();
    }
}