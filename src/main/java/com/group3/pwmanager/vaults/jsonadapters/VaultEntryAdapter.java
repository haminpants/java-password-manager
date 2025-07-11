package com.group3.pwmanager.vaults.jsonadapters;

import com.google.gson.*;
import com.group3.pwmanager.vaults.VaultEntry;

import java.lang.reflect.Type;

public class VaultEntryAdapter implements JsonSerializer<VaultEntry>, JsonDeserializer<VaultEntry> {
    @Override
    public JsonElement serialize (VaultEntry entry, Type t, JsonSerializationContext context) {
        JsonArray jsonTags = new JsonArray();
        entry.getTags().forEach(jsonTags::add);

        JsonObject jsonEntry = new JsonObject();
        jsonEntry.addProperty("title", entry.getTitle());
        jsonEntry.addProperty("username", entry.getUsername());
        jsonEntry.addProperty("password", entry.getPassword());
        jsonEntry.addProperty("note", entry.getNote());
        jsonEntry.add("tags", jsonTags);
        return jsonEntry;
    }

    @Override
    public VaultEntry deserialize (JsonElement jsonElement, Type t, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        VaultEntry entry = new VaultEntry(json.get("title").getAsString(), json.get("username")
            .getAsString(), json.get("password").getAsString(), json.get("note").getAsString());
        json.get("tags").getAsJsonArray().forEach(e -> entry.addTag(e.getAsString()));
        return entry;
    }
}