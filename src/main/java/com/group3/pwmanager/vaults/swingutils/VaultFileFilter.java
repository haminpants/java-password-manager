package com.group3.pwmanager.vaults.swingutils;

import com.group3.pwmanager.vaults.Vault;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class VaultFileFilter extends FileFilter {
    @Override
    public boolean accept (File file) {
        return file.getName().toLowerCase().endsWith("." + Vault.FILE_EXTENSION);
    }

    @Override
    public String getDescription () {
        return "Java Password Manager Vault (*." + Vault.FILE_EXTENSION + ")";
    }
}