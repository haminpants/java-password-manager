package com.group3.pwmanager;

import com.formdev.flatlaf.FlatDarkLaf;
import com.group3.pwmanager.forms.HomeMenu;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatDarkLaf.setup();

        JFrame applicationFrame = new JFrame();
        applicationFrame.setTitle("Password Manager");
        applicationFrame.setContentPane(new HomeMenu(applicationFrame).getContentPane());
        applicationFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        applicationFrame.pack();
        applicationFrame.setVisible(true);
    }
}