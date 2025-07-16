package com.group3.pwmanager.vault.adapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.group3.pwmanager.vault.VaultEntry;
import com.group3.pwmanager.vault.VaultEntryTableModel;

import java.lang.reflect.Type;

public class VaultEntryTableModelAdapter implements JsonSerializer<VaultEntryTableModel> {
    @Override
    public JsonElement serialize (VaultEntryTableModel tableModel, Type t, JsonSerializationContext c) {
        JsonArray json = new JsonArray();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            json.add(c.serialize(tableModel.get(i), VaultEntry.class));
        }
        return json;
    }
}