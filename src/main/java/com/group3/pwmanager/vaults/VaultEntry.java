package com.group3.pwmanager.vaults;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.LinkedHashSet;

public class VaultEntry implements JsonSerializer<VaultEntry> {
    private String title;
    private String username;
    private String password;
    private String note;
    private LinkedHashSet<String> tags = new LinkedHashSet<>();

    public VaultEntry (String title, String username, String password, String note) {
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

    @Override
    public JsonElement serialize (VaultEntry entry, Type type, JsonSerializationContext context) {
        JsonArray tagsJson = new JsonArray();
        entry.tags.forEach(tagsJson::add);

        JsonObject entryJson = new JsonObject();
        entryJson.addProperty("title", entry.title);
        entryJson.addProperty("username", entry.username);
        entryJson.addProperty("password", entry.password);
        entryJson.add("tags", tagsJson);
        return entryJson;
    }
}