package com.group3.pwmanager.vaults.jsonadapters;

import com.google.gson.*;
import com.group3.pwmanager.vaults.VaultSettings;

import java.lang.reflect.Type;

public class VaultSettingsAdapter implements JsonSerializer<VaultSettings>, JsonDeserializer<VaultSettings> {
    @Override
    public JsonElement serialize (VaultSettings settings, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonSettings = new JsonObject();
        jsonSettings.addProperty("minimizeOnClose", settings.isMinimizeOnClose());
        return jsonSettings;
    }

    @Override
    public VaultSettings deserialize (JsonElement jsonElement, Type t, JsonDeserializationContext context) throws JsonParseException {
        JsonObject json = jsonElement.getAsJsonObject();
        VaultSettings settings = new VaultSettings();
        settings.setMinimizeOnClose(json.get("minimizeOnClose").getAsBoolean());
        return settings;
    }
}