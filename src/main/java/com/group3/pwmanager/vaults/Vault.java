package com.group3.pwmanager.vaults;

import java.util.*;
import java.util.function.BiConsumer;

public class Vault {
    private int idCounter = 0;
    private String name;
    private LinkedHashMap<Integer, VaultEntry> entries = new LinkedHashMap<>();
    private VaultSettings settings;

    public Vault (String name) {
        this.name = name;
        this.settings = new VaultSettings();
    }

    public void addEntry (VaultEntry entry) {
        entries.put(idCounter++, entry);
    }

    public void removeEntry (int id) {
        entries.remove(id);
    }

    public void forEachEntry (BiConsumer<? super Integer, ? super VaultEntry> consumer) {
        entries.forEach(consumer);
    }

    public String getName () {
        return name;
    }

    public Optional<VaultEntry> getEntry (int id) {
        return Optional.ofNullable(entries.get(id));
    }

    public VaultSettings getSettings () {
        return settings;
    }
}