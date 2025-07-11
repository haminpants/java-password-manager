package com.group3.pwmanager.vaults.jsonadapters;

import com.google.gson.*;
import com.group3.pwmanager.vaults.Vault;
import com.group3.pwmanager.vaults.VaultEntry;
import com.group3.pwmanager.vaults.VaultSettings;

import java.lang.reflect.Type;

public class VaultAdapter implements JsonSerializer<Vault> {
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
}