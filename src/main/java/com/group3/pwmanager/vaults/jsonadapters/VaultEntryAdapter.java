package com.group3.pwmanager.vaults.jsonadapters;

import com.google.gson.*;
import com.group3.pwmanager.vaults.VaultEntry;

import java.lang.reflect.Type;

public class VaultEntryAdapter implements JsonSerializer<VaultEntry> {
    @Override
    public JsonElement serialize (VaultEntry entry, Type t, JsonSerializationContext context) {
        JsonArray jsonTags = new JsonArray();
        entry.getTags().forEach(jsonTags::add);

        JsonObject jsonEntry = new JsonObject();
        jsonEntry.addProperty("title", entry.getTitle());
        jsonEntry.addProperty("username", entry.getUsername());
        jsonEntry.addProperty("password", entry.getPassword());
        jsonEntry.add("tags", jsonTags);
        return jsonEntry;
    }
}