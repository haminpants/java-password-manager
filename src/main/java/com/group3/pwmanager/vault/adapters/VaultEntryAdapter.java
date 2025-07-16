package com.group3.pwmanager.vault.adapters;

import com.google.gson.*;
import com.group3.pwmanager.vault.VaultEntry;

import java.lang.reflect.Type;

public class VaultEntryAdapter implements JsonSerializer<VaultEntry>, JsonDeserializer<VaultEntry> {
    @Override
    public JsonElement serialize (VaultEntry vaultEntry, Type t, JsonSerializationContext c) {
        JsonObject json = new JsonObject();
        json.addProperty("title", vaultEntry.getTitle());
        json.addProperty("username", vaultEntry.getUsername());
        json.addProperty("password", vaultEntry.getPassword());
        json.addProperty("note", vaultEntry.getNote());
        return json;
    }

    @Override
    public VaultEntry deserialize (JsonElement jsonElement, Type t, JsonDeserializationContext c) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        String title = json.get("title").getAsString();
        String username = json.get("username").getAsString();
        String password = json.get("password").getAsString();
        String note = json.get("note").getAsString();
        return new VaultEntry(title, username, password, note);
    }
}