package com.group3.pwmanager;

import java.util.LinkedHashSet;

public class VaultEntry {
    private String title;
    private String username;
    private String password;
    private String note;
    private LinkedHashSet<String> tags;

    public VaultEntry(String title, String username, String password, String note) {
        this.title = title;
        this.username = username;
        this.password = password;
        this.note = note;
    }

    public boolean addTag (String tag) {
        return tags.add(tag);
    }
    public boolean removeTag (String tag) {
        return tags.remove(tag);
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNote() {
        return note;
    }
}