package com.group3.pwmanager.vault;

import com.group3.pwmanager.HomeMenu;

import javax.crypto.SecretKey;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VaultBuilder {
    private String name = "New Vault";
    private File file;
    private final List<VaultEntry> entries = new ArrayList<>();

    public VaultBuilder () { }

    public VaultBuilder setName (String name) {
        this.name = name;
        return this;
    }

    public VaultBuilder setFile (File file) {
        this.file = file;
        return this;
    }

    public VaultBuilder addEntry (VaultEntry entry) {
        entries.add(entry);
        return this;
    }

    public Vault build (HomeMenu owner, SecretKey key) {
        if (file != null && file.isDirectory()) throw new IllegalStateException("Vault file cannot be a directory.");
        return new Vault(owner, key, name, file, entries);
    }
}