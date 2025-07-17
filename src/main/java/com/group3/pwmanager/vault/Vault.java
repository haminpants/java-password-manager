package com.group3.pwmanager.vault;

import com.group3.pwmanager.EncryptionUtils;
import com.group3.pwmanager.HomeMenu;
import com.group3.pwmanager.vault.swingutils.VaultTableCellRenderer;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Vault {
    public static final String FILE_EXTENSION = "jpmv";

    private JPanel pnl_main;
    private JTable tbl_entries;
    private JButton btn_addEntry;
    private JButton btn_editEntry;
    private JButton btn_copyPassword;

    private final HomeMenu owner;
    private final JFrame frame;
    private SecretKey key;
    private File file;

    private String name = "New Vault";
    private final VaultEntryTableModel tableModel = new VaultEntryTableModel();

    public Vault (HomeMenu owner, SecretKey key) {
        // Set up JFrame
        this.frame = new JFrame();
        frame.setTitle(getWindowTitle());
        frame.setContentPane(pnl_main);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set up table
        tbl_entries.setModel(tableModel);
        tbl_entries.getTableHeader().setReorderingAllowed(false);
        tbl_entries.setDefaultRenderer(Object.class, new VaultTableCellRenderer());
        tbl_entries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tbl_entries.getSelectionModel().addListSelectionListener(e -> updateEntryButtons());

        tbl_entries.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "focus_next");
        tbl_entries.getActionMap().put("focus_next", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                int nextRowIndex = tbl_entries.getSelectedRow() + 1;
                if (nextRowIndex == tbl_entries.getRowCount()) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                    tbl_entries.clearSelection();
                }
                else tbl_entries.setRowSelectionInterval(nextRowIndex, nextRowIndex);
            }
        });

        tbl_entries.getInputMap(JComponent.WHEN_FOCUSED)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK), "focus_previous");
        tbl_entries.getActionMap().put("focus_previous", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                if (tbl_entries.getSelectedRow() == 0) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
                    tbl_entries.clearSelection();
                }
                else {
                    int rowIndex = tbl_entries.getSelectedRow() - 1;
                    if (rowIndex < 0) rowIndex = tbl_entries.getRowCount() - 1;
                    tbl_entries.setRowSelectionInterval(rowIndex, rowIndex);
                }
            }
        });

        // Set up buttons
        btn_addEntry.addActionListener(event -> {
            VaultEntryDialogue dialogue = new VaultEntryDialogue(this);
            dialogue.setVisible(true);
        });

        btn_editEntry.addActionListener(event -> {
            int index = tbl_entries.getSelectedRow();
            if (index == -1) {
                updateEntryButtons();
                return;
            }

            VaultEntryDialogue dialogue = new VaultEntryDialogue(this, tableModel.get(index));
            dialogue.setVisible(true);
        });

        btn_copyPassword.addActionListener(event -> {
            int index = tbl_entries.getSelectedRow();
            if (index == -1) {
                updateEntryButtons();
                return;
            }
            copyPassword(index);
        });

        // Set up key bindings
        pnl_main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "clear_selection");
        pnl_main.getActionMap().put("clear_selection", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                if (tbl_entries.getSelectedRow() != -1) tbl_entries.clearSelection();
            }
        });

        pnl_main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save");
        pnl_main.getActionMap().put("save", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                save();
            }
        });

        tbl_entries.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copy_pw");
        tbl_entries.getActionMap().put("copy_pw", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                copyPassword(tbl_entries.getSelectedRow());
            }
        });

        // Parameters
        this.owner = owner;
        this.key = key;
    }

    public Vault (HomeMenu owner, SecretKey key, String name, File file, List<VaultEntry> entries) {
        this(owner, key);
        this.name = name;
        this.file = file;
        tableModel.addEntries(entries);
    }

    public void save () {
        if (file == null) {
            JFileChooser fileChooser = owner.getFileChooser();
            fileChooser.setSelectedFile(new File(name + "." + FILE_EXTENSION));
            if (fileChooser.showSaveDialog(pnl_main) != JFileChooser.APPROVE_OPTION) return;
            file = fileChooser.getSelectedFile();
            if (!file.getName().endsWith("." + FILE_EXTENSION)) file = new File(file + "." + FILE_EXTENSION);
            fileChooser.setSelectedFile(null);
        }

        try (FileWriter fw = new FileWriter(file); BufferedWriter writer = new BufferedWriter(fw)) {
            writer.write(EncryptionUtils.encrypt(owner.getGson().toJson(this, Vault.class), key));
        }
        catch (IOException e) {
            // TODO: implement robust handling
            throw new RuntimeException(e);
        }
        catch (Exception e) {
            // TODO: implement robust handling
            throw new RuntimeException(e);
        }
    }

    private void copyPassword (int entryIndex) {
        if (entryIndex < 0 || entryIndex > tableModel.getRowCount()) return;
        Toolkit.getDefaultToolkit()
            .getSystemClipboard()
            .setContents(new StringSelection(tableModel.get(entryIndex).getPassword()), null);
    }

    private void updateEntryButtons () {
        btn_editEntry.setEnabled(tbl_entries.getSelectedRow() != -1);
        btn_copyPassword.setEnabled(tbl_entries.getSelectedRow() != -1);
    }

    public void add (VaultEntry entry) {
        tableModel.addEntry(entry);
    }

    public String getName () {
        return name;
    }

    private String getWindowTitle () {
        return name + " - Java Password Manager";
    }

    protected JFrame getFrame () {
        return frame;
    }

    public VaultEntryTableModel getTableModel () {
        return tableModel;
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setVisible (boolean visible) {
        frame.setVisible(visible);
    }
}