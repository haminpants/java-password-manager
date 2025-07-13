package com.group3.pwmanager;

import javax.swing.*;

public interface Menu {
    JPanel getContentPane ();

    String getTitle ();

    JMenuBar getMenuBar ();
}