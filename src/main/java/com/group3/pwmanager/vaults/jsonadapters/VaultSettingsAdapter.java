package com.group3.pwmanager.vaults.jsonadapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.group3.pwmanager.vaults.VaultSettings;

import java.lang.reflect.Type;

public class VaultSettingsAdapter implements JsonSerializer<VaultSettings> {
    @Override
    public JsonElement serialize (VaultSettings settings, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonSettings = new JsonObject();
        jsonSettings.addProperty("minimizeOnClose", settings.isMinimizeOnClose());
        return jsonSettings;
    }
}