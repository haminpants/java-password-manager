package com.group3.pwmanager;

import java.util.ArrayList;
import java.util.List;

public class Vault {
    private String name;
    private List<VaultEntry> entries;
    private VaultSettings settings;

    public Vault(String name) {
        this.name = name;
        this.entries = new ArrayList<>();
        this.settings = new VaultSettings();
    }

    public boolean addEntry(VaultEntry entry) {
        return entries.add(entry);
    }

    public boolean removeEntry(VaultEntry entry) {
        return entries.remove(entry);
    }

    public String getName() {
        return name;
    }

    public List<VaultEntry> getEntries() {
        return entries;
    }

    public VaultSettings getSettings() {
        return settings;
    }
}