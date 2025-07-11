package com.group3.pwmanager.vaults;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class Vault {
    private int idCounter = 0;
    private String name;
    private final LinkedHashMap<Integer, VaultEntry> entries = new LinkedHashMap<>();
    private final VaultSettings settings;

    public Vault (String name, Collection<VaultEntry> entries, VaultSettings settings) {
        this.name = name;
        this.settings = settings;
        entries.forEach(this::addEntry);
    }

    public Vault (String name) {
        this(name, List.of(), new VaultSettings());
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