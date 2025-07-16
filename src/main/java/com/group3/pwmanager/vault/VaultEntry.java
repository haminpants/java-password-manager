package com.group3.pwmanager.vault;

public class VaultEntry {
    private String title = "";
    private String username = "";
    private String password = "";
    private String note = "";

    public VaultEntry (String title, String username, String password, String note) {
        this.title = title;
        this.username = username;
        this.password = password;
        this.note = note;
    }

    public String getTitle () {
        return title;
    }

    public String getUsername () {
        return username;
    }

    public String getPassword () {
        return password;
    }

    public String getNote () {
        return note;
    }

    protected void setTitle (String title) {
        this.title = title;
    }

    protected void setUsername (String username) {
        this.username = username;
    }

    protected void setPassword (String password) {
        this.password = password;
    }

    protected void setNote (String note) {
        this.note = note;
    }
}