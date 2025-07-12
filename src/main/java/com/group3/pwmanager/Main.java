package com.group3.pwmanager;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.group3.pwmanager.vaults.VaultMenu;
import com.group3.pwmanager.vaults.Vault;
import com.group3.pwmanager.vaults.VaultEntry;
import com.group3.pwmanager.vaults.VaultSettings;
import com.group3.pwmanager.vaults.jsonadapters.VaultAdapter;
import com.group3.pwmanager.vaults.jsonadapters.VaultEntryAdapter;
import com.group3.pwmanager.vaults.jsonadapters.VaultSettingsAdapter;

import javax.swing.*;

public class Main {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
        .registerTypeAdapter(Vault.class, new VaultAdapter())
        .registerTypeAdapter(VaultEntry.class, new VaultEntryAdapter())
        .registerTypeAdapter(VaultSettings.class, new VaultSettingsAdapter()).create();
    private static final JFrame appFrame = new JFrame();

    public static void main (String[] args) {
        FlatDarkLaf.setup();
        setAppContent(new HomeMenu());

        appFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        appFrame.setLocationRelativeTo(null);
        appFrame.setVisible(true);
    }

    public static void setAppContent (Menu menu) {
        appFrame.setContentPane(menu.getContentPane());
        appFrame.setPreferredSize(menu.getPreferredSize());
        appFrame.setJMenuBar(menu.getMenuBar());
        appFrame.pack();
        appFrame.revalidate();
    }
}