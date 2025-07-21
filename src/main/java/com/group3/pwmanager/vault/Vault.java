package com.group3.pwmanager.vault;

import com.group3.pwmanager.EncryptionUtils;
import com.group3.pwmanager.HomeMenu;
import com.group3.pwmanager.vault.swingutils.VaultTableCellRenderer;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
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

    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenuItem fileSaveItem = new JMenuItem("Save", KeyEvent.VK_S);
    private JMenuItem fileCloseItem = new JMenuItem("Close Vault", KeyEvent.VK_C);

    private final HomeMenu owner;
    private final JFrame frame;
    private final SecretKey key;
    private File file;
    private boolean unsavedChanges = false;

    private String name = "New Vault";
    private final VaultEntryTableModel tableModel = new VaultEntryTableModel();

    public Vault (HomeMenu owner, SecretKey key) {
        // Set up JFrame
        this.frame = new JFrame();
        frame.setTitle(getWindowTitle());
        frame.setContentPane(pnl_main);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing (WindowEvent e) {
                if (!unsavedChanges || JOptionPane.showConfirmDialog(frame,
                    "You have unsaved changes. Are you sure you want to quit?", "Warning", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) System.exit(0);
            }
        });

        // Set up table
        tbl_entries.setModel(tableModel);
        tbl_entries.getTableHeader().setReorderingAllowed(false);
        tbl_entries.setDefaultRenderer(Object.class, new VaultTableCellRenderer());
        tbl_entries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl_entries.getSelectionModel().addListSelectionListener(e -> updateEntryButtons());

        // Set up menu bar
        frame.setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        fileMenu.setMnemonic(KeyEvent.VK_F);

        fileMenu.add(fileSaveItem);
        fileSaveItem.addActionListener(action -> save());

        fileMenu.add(fileCloseItem);
        fileCloseItem.addActionListener(action -> {
            if (!unsavedChanges || JOptionPane.showConfirmDialog(frame,
                "You have unsaved changes. Are you sure you want to close this vault?", "Warning",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                frame.dispose();
                owner.setVisible(true);
            }
        });

        // Set up buttons
        btn_addEntry.addActionListener(event -> showCreateEntryDialogue());
        btn_editEntry.addActionListener(event -> showEditEntryDialogue());

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

        pnl_main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "create_entry");
        pnl_main.getActionMap().put("create_entry", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                showCreateEntryDialogue();
            }
        });

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

        tbl_entries.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copy_pw");
        tbl_entries.getActionMap().put("copy_pw", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                copyPassword(tbl_entries.getSelectedRow());
            }
        });

        tbl_entries.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK), "edit_entry");
        tbl_entries.getActionMap().put("edit_entry", new AbstractAction() {
            @Override
            public void actionPerformed (ActionEvent e) {
                showEditEntryDialogue();
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
            setUnsavedChanges(false);
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

    private void showCreateEntryDialogue () {
        VaultEntryDialogue dialogue = new VaultEntryDialogue(this);
        dialogue.setVisible(true);
    }

    private void showEditEntryDialogue () {
        int index = tbl_entries.getSelectedRow();
        if (index == -1) {
            updateEntryButtons();
            return;
        }

        VaultEntryDialogue dialogue = new VaultEntryDialogue(this, tableModel.get(index));
        dialogue.setVisible(true);
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
        return (unsavedChanges ? "*" : "") + name + " - Java Password Manager";
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

    public void setUnsavedChanges (boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
        frame.setTitle(getWindowTitle());
    }

    {
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$ () {
        pnl_main = new JPanel();
        pnl_main.setLayout(new GridLayoutManager(3, 4, new Insets(0, 15, 15, 15), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        pnl_main.add(scrollPane1,
            new GridConstraints(2, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
                new Dimension(900, 500), null, 0, false));
        tbl_entries = new JTable();
        scrollPane1.setViewportView(tbl_entries);
        btn_addEntry = new JButton();
        btn_addEntry.setIcon(new ImageIcon(getClass().getResource("/form_assets/add_32px.png")));
        btn_addEntry.setText("");
        btn_addEntry.setToolTipText("Add entry (Ctrl + N)");
        pnl_main.add(btn_addEntry,
            new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_editEntry = new JButton();
        btn_editEntry.setEnabled(false);
        btn_editEntry.setIcon(new ImageIcon(getClass().getResource("/form_assets/edit_32px.png")));
        btn_editEntry.setText("");
        btn_editEntry.setToolTipText("Edit entry (Ctrl + E)");
        pnl_main.add(btn_editEntry,
            new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        pnl_main.add(separator1,
            new GridConstraints(1, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        btn_copyPassword = new JButton();
        btn_copyPassword.setEnabled(false);
        btn_copyPassword.setIcon(new ImageIcon(getClass().getResource("/form_assets/copy_32px.png")));
        btn_copyPassword.setText("");
        btn_copyPassword.setToolTipText("Copy password (Ctrl + C)");
        pnl_main.add(btn_copyPassword,
            new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, 1,
                GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        pnl_main.add(panel1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /** @noinspection ALL */
    public JComponent $$$getRootComponent$$$ () { return pnl_main; }
}