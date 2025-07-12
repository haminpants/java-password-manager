package com.group3.pwmanager.vaults.swingutils;

import javax.swing.table.DefaultTableModel;

public class VaultTableModel extends DefaultTableModel {
    @Override
    public boolean isCellEditable (int row, int column) {
        return false;
    }
}