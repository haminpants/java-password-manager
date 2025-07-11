package com.group3.pwmanager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.pwmanager.forms.Menu;
import com.group3.pwmanager.vaults.VaultMenu;
import com.group3.pwmanager.vaults.Vault;
import com.group3.pwmanager.vaults.VaultEntry;

import javax.swing.*;

public class Main {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final JFrame appFrame = new JFrame();

    public static void main (String[] args) {
        FlatDarkLaf.setup();

        Vault vault = new Vault("Test Vault");
        vault.getSettings().setMinimizeOnClose(true);
        vault.addEntry(new VaultEntry("Test 1", "test@test.com", "wow", "hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world "));
        vault.addEntry(new VaultEntry("Test 2", "test@test.com", "cool!", "hello world"));
        vault.addEntry(new VaultEntry("Test 3", "test@test.com", "cooler", "hello world"));
        vault.addEntry(new VaultEntry("Test 4", "test@test.com", "damn", "hello world"));
        vault.addEntry(new VaultEntry("Test 5", "test@test.com", "pls work", "hello world"));
        vault.addEntry(new VaultEntry("Test 6", "test@test.com", "it work!!!1", "hello world"));
        vault.addEntry(new VaultEntry("Test 7", "test@test.com", "yay :)", "hello world"));
        VaultMenu menu = new VaultMenu(vault);
        setAppContent(menu);

        appFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        appFrame.setLocationRelativeTo(null);
        appFrame.setVisible(true);

        vault.getEntry(0).ifPresent(e -> System.out.println(GSON.toJson(e)));
    }

    public static void setAppContent (Menu menu) {
        appFrame.setContentPane(menu.getContentPane());
        appFrame.setPreferredSize(menu.getPreferredSize());
        appFrame.pack();
        appFrame.revalidate();
    }
}