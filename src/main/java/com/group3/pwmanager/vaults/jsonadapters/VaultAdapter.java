package com.group3.pwmanager.vaults.jsonadapters;

import com.google.gson.*;
import com.group3.pwmanager.vaults.Vault;
import com.group3.pwmanager.vaults.VaultEntry;
import com.group3.pwmanager.vaults.VaultSettings;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VaultAdapter implements JsonSerializer<Vault>, JsonDeserializer<Vault> {
    @Override
    public JsonElement serialize (Vault vault, Type t, JsonSerializationContext context) {
        JsonArray jsonEntries = new JsonArray();
        vault.forEachEntry((id, entry) -> jsonEntries.add(context.serialize(entry, VaultEntry.class)));

        JsonObject jsonVault = new JsonObject();
        jsonVault.addProperty("name", vault.getName());
        jsonVault.add("entries", jsonEntries);
        jsonVault.add("settings", context.serialize(vault.getSettings(), VaultSettings.class));

        return jsonVault;
    }

    @Override
    public Vault deserialize (JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        String name = json.get("name").getAsString();
        List<VaultEntry> entries = new ArrayList<>();
        json.get("entries").getAsJsonArray()
            .forEach(jsonEntry -> entries.add(context.deserialize(jsonEntry, VaultEntry.class)));
        VaultSettings settings = context.deserialize(json.get("settings"), VaultSettings.class);
        return new Vault(name, entries, settings);
    }
}