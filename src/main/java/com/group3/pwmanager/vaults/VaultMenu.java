package com.group3.pwmanager.vaults;

import com.group3.pwmanager.Main;
import com.group3.pwmanager.Menu;
import com.group3.pwmanager.vaults.swingutils.VaultFileFilter;
import com.group3.pwmanager.vaults.swingutils.VaultTableCellRenderer;
import com.group3.pwmanager.vaults.swingutils.VaultTableModel;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VaultMenu implements Menu, ActionListener {
    private JPanel pnl_main;
    private JTable tbl_entries;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;

    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem fileSaveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);

    private final Vault vault;
    private final TableColumnModel columnModel;
    private final VaultTableModel entryTable = new VaultTableModel();
    private final VaultTableCellRenderer cellRenderer = new VaultTableCellRenderer();

    public VaultMenu (Vault vault) {
        this.vault = vault;
        this.columnModel = tbl_entries.getColumnModel();

        // Entry table setup
        entryTable.setColumnIdentifiers(new Object[]{"ID", "Title", "Username", "Notes"});
        tbl_entries.setModel(entryTable);
        tbl_entries.setDefaultRenderer(Object.class, cellRenderer);
        tbl_entries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl_entries.removeColumn(columnModel.getColumn(0));
        vault.forEachEntry((id, entry) -> entryTable.addRow(new Object[]{id, entry.getTitle(), entry.getUsername(), entry.getNote()}));

        // Menu bar setup
        menuBar.add(fileMenu);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.add(fileSaveMenuItem);
        fileSaveMenuItem.setActionCommand("file_save");
        fileSaveMenuItem.addActionListener(this);

        // Input map and action map setup
        InputMap inputMap = pnl_main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear_selection");
        pnl_main.getActionMap().put("clear_selection", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                tbl_entries.clearSelection();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save");
        pnl_main.getActionMap().put("save", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                save();
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

    private boolean save () {
        // If the vault doesn't have an associated file, prompt the user to create a new one
        if (vault.getFile() == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileFilter(new VaultFileFilter());
            fileChooser.setSelectedFile(new File(vault.getName() + "." + Vault.FILE_EXTENSION));
            if (fileChooser.showSaveDialog(pnl_main) != JFileChooser.APPROVE_OPTION) return false;

            File file = fileChooser.getSelectedFile();
            file = !file.getName().endsWith("." + Vault.FILE_EXTENSION)
                ? new File(file + "." + Vault.FILE_EXTENSION)
                : file;
            vault.setFile(file);
        }

        try (FileWriter fileWriter = new FileWriter(vault.getFile());
            BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(Main.GSON.toJson(vault, Vault.class));
            System.out.println(vault.getFile());
            return true;
        }
        catch (IOException e) {
            // TODO: more robust error handling
            System.out.println("Failed to save");
            return false;
        }
    }

    @Override
    public void actionPerformed (ActionEvent e) {
        if (e.getActionCommand().equals(fileSaveMenuItem.getActionCommand())) {
            save();
        }
    }

    @Override
    public JPanel getContentPane () {
        return pnl_main;
    }

    @Override
    public String getTitle () {
        return vault.getName() + " - Java Password Manager";
    }

    @Override
    public JMenuBar getMenuBar () {
        return menuBar;
    }
}