package com.group3.pwmanager.vaults;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class VaultSettings implements JsonSerializer<VaultSettings> {
    private boolean minimizeOnClose = false;

    public boolean isMinimizeOnClose () {
        return minimizeOnClose;
    }

    public void setMinimizeOnClose (boolean minimizeOnClose) {
        this.minimizeOnClose = minimizeOnClose;
    }

    @Override
    public JsonElement serialize (VaultSettings settings, Type t, JsonSerializationContext context) {
        // TODO: finish this
        return null;
    }
}