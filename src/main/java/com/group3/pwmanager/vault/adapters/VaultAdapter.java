package com.group3.pwmanager.vault.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.group3.pwmanager.vault.Vault;
import com.group3.pwmanager.vault.VaultEntryTableModel;

import java.lang.reflect.Type;

public class VaultAdapter implements JsonSerializer<Vault> {
    @Override
    public JsonElement serialize (Vault vault, Type t, JsonSerializationContext c) {
        JsonObject json = new JsonObject();
        json.addProperty("name", vault.getName());
        json.add("entries", c.serialize(vault.getTableModel(), VaultEntryTableModel.class));
        return json;
    }
}