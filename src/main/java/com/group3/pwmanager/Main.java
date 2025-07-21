package com.group3.pwmanager;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.io.PrintWriter;

public class Main {
    public static void main (String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            SwingUtilities.invokeLater(() -> new HomeMenu().setVisible(true));
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            try (PrintWriter out = new PrintWriter("error.log")) {
                e.printStackTrace(out);
            }
            catch (Exception ioEx) {
                ioEx.printStackTrace();
            }
        }
    }
}