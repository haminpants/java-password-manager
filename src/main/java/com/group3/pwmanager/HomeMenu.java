package com.group3.pwmanager;

import javax.swing.*;
import java.awt.*;

public class HomeMenu {
    public static final Dimension PREFERRED_SIZE = new Dimension(300, 400);

    private JPanel contentPane;
    private JButton btn_newVault;
    private JButton btn_loadVault;
    private JList<String> lst_recentVaults;

    public HomeMenu() {

    }

    public JPanel getContentPane() {
        return contentPane;
    }
}