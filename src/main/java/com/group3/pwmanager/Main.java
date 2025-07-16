package com.group3.pwmanager;

import com.formdev.flatlaf.FlatDarkLaf;

public class Main {
    public static void main (String[] args) {
        FlatDarkLaf.setup();
        HomeMenu menu = new HomeMenu();
        menu.setVisible(true);
    }
}