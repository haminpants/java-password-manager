package com.group3.pwmanager.forms;

import com.group3.pwmanager.Vault;
import com.group3.pwmanager.VaultEntry;
import com.group3.pwmanager.VaultTableCellRenderer;
import com.group3.pwmanager.VaultTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class VaultMenu implements Menu {
    private JPanel pnl_main;
    private JTable tbl_entries;

    private final Vault vault;
    private final VaultTableModel entryTable = new VaultTableModel();
    private final VaultTableCellRenderer cellRenderer = new VaultTableCellRenderer();

    public VaultMenu(Vault vault) {
        this.vault = vault;

        setupEntryTable();
        InputMap inputMap = pnl_main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = pnl_main.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear_selection");
        actionMap.put("clear_selection", new AbstractAction("clear_selection") {
            @Override
            public void actionPerformed(ActionEvent e) {
                tbl_entries.clearSelection();
            }
        });
    }

    public VaultMenu() {
        this(new Vault("New Vault"));
    }

    private void setupEntryTable() {
        if (tbl_entries == null) return;

        tbl_entries.setModel(entryTable);
        tbl_entries.setDefaultRenderer(Object.class, cellRenderer);
        entryTable.setColumnIdentifiers(new Object[]{"Title", "Username", "Notes"});
        entryTable.setRowCount(0);
        for (VaultEntry entry : vault.getEntries()) {
            entryTable.addRow(new Object[]{entry.getTitle(), entry.getUsername(), entry.getNote()});
        }
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