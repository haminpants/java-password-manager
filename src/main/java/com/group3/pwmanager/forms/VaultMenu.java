package com.group3.pwmanager.forms;

import com.group3.pwmanager.Vault;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VaultMenu implements Menu {
    private JPanel pnl_main;
    private JTable tbl_entries;

    private final Vault vault;
    private final DefaultTableModel entryTable = new DefaultTableModel();

    public VaultMenu(Vault vault) {
        this.vault = vault;

        tbl_entries.setModel(entryTable);
        entryTable.setColumnIdentifiers(new Object[]{"Title", "Username", "Notes"});
    }

    public VaultMenu() {
        this(new Vault("New Vault"));
    }

    @Override
    public JPanel getContentPane() {
        return pnl_main;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 600);
    }
}