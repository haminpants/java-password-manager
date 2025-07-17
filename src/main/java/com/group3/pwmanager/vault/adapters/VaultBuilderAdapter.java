package com.group3.pwmanager.vault.adapters;

import com.google.gson.*;
import com.group3.pwmanager.vault.VaultBuilder;
import com.group3.pwmanager.vault.VaultEntry;

import java.lang.reflect.Type;

public class VaultBuilderAdapter implements JsonDeserializer<VaultBuilder> {
    @Override
    public VaultBuilder deserialize (JsonElement jsonElement, Type t, JsonDeserializationContext c) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        VaultBuilder vaultBuilder = new VaultBuilder();
        vaultBuilder.setName(json.get("name").getAsString());
        json.get("entries")
            .getAsJsonArray()
            .forEach(entry -> vaultBuilder.addEntry(c.deserialize(entry, VaultEntry.class)));
        return vaultBuilder;
    }
}