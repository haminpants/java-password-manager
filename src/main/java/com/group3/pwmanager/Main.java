package com.group3.pwmanager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.group3.pwmanager.forms.HomeMenu;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        JFrame appFrame = new JFrame("Password Manager");
        appFrame.setContentPane(new HomeMenu(appFrame).getContentPane());
        appFrame.setPreferredSize(HomeMenu.PREFERRED_SIZE);
        appFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        appFrame.pack();
        appFrame.setLocationRelativeTo(null);
        appFrame.setResizable(false);
        appFrame.setVisible(true);
    }
}