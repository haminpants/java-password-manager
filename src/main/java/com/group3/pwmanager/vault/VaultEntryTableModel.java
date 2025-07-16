package com.group3.pwmanager.vault;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VaultEntryTableModel extends AbstractTableModel {
    List<String> columnNames = List.of("Title", "Username", "Note");
    List<VaultEntry> entries = new ArrayList<>();

    public VaultEntryTableModel () { }

    public void addEntry (VaultEntry entry) {
        entries.add(entry);
        int rowIndex = entries.size() - 1;
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    public void addEntries (Collection<VaultEntry> entries) {
        this.entries.addAll(entries);
        fireTableDataChanged();
    }

    public void updateEntry (VaultEntry entry) {
        int index = entries.indexOf(entry);
        if (index == -1) return;
        fireTableRowsUpdated(index, index);
    }

    public VaultEntry get (int row) {
        return entries.get(row);
    }

    @Override
    public int getRowCount () {
        return entries.size();
    }

    @Override
    public int getColumnCount () {
        return columnNames.size();
    }

    @Override
    public String getColumnName (int column) {
        return columnNames.get(column);
    }

    @Override
    public Object getValueAt (int rowIndex, int columnIndex) {
        VaultEntry entry = entries.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> entry.getTitle();
            case 1 -> entry.getUsername();
            case 2 -> entry.getNote();
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable (int rowIndex, int columnIndex) {
        return false;
    }
}