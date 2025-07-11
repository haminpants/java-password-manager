package com.group3.pwmanager.vaults;

import com.group3.pwmanager.Menu;
import com.group3.pwmanager.vaults.swingutils.VaultTableCellRenderer;
import com.group3.pwmanager.vaults.swingutils.VaultTableModel;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class VaultMenu implements Menu {
    private JPanel pnl_main;
    private JTable tbl_entries;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;

    private final Vault vault;
    private final TableColumnModel columnModel;
    private final VaultTableModel entryTable = new VaultTableModel();
    private final VaultTableCellRenderer cellRenderer = new VaultTableCellRenderer();

    public VaultMenu (Vault vault) {
        this.vault = vault;
        this.columnModel = tbl_entries.getColumnModel();

        entryTable.setColumnIdentifiers(new Object[]{"ID", "Title", "Username", "Notes"});
        tbl_entries.setModel(entryTable);
        tbl_entries.setDefaultRenderer(Object.class, cellRenderer);
        tbl_entries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl_entries.removeColumn(columnModel.getColumn(0));
        vault.forEachEntry((id, entry) -> entryTable.addRow(new Object[]{id, entry.getTitle(), entry.getUsername(), entry.getNote()}));

        InputMap inputMap = pnl_main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = pnl_main.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear_selection");
        actionMap.put("clear_selection", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                tbl_entries.clearSelection();
            }
        });

        tbl_entries.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "focus_next");
        tbl_entries.getActionMap().put("focus_next", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                if (tbl_entries.getSelectedRow() == tbl_entries.getRowCount() - 1) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                    tbl_entries.clearSelection();
                }
                else {
                    int nextRow = tbl_entries.getSelectedRow() + 1;
                    tbl_entries.setRowSelectionInterval(nextRow, nextRow);
                }
            }
        });

        tbl_entries.getInputMap()
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK), "focus_prev");
        tbl_entries.getActionMap().put("focus_prev", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                if (tbl_entries.getSelectedRow() == 0) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
                    tbl_entries.clearSelection();
                }
                else {
                    int prevRow = tbl_entries.getSelectedRow() - 1;
                    if (prevRow < 0) prevRow = tbl_entries.getRowCount() - 1;
                    tbl_entries.setRowSelectionInterval(prevRow, prevRow);
                }
            }
        });

        tbl_entries.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copy_pw");
        tbl_entries.getActionMap().put("copy_pw", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                if (tbl_entries.getSelectedRow() == -1) return;
                int entryId = (int) entryTable.getValueAt(tbl_entries.getSelectedRow(), 0);
                vault.getEntry(entryId).ifPresent(entry -> Toolkit.getDefaultToolkit().getSystemClipboard()
                    .setContents(new StringSelection(entry.getPassword()), null));
                if (vault.getSettings().isMinimizeOnClose())
                    ((JFrame) SwingUtilities.getWindowAncestor(pnl_main)).setState(Frame.ICONIFIED);
            }
        });
    }

    public VaultMenu () {
        this(new Vault("New Vault"));
    }

    @Override
    public JPanel getContentPane () {
        return pnl_main;
    }

    @Override
    public Dimension getPreferredSize () {
        return new Dimension(900, 600);
    }
}