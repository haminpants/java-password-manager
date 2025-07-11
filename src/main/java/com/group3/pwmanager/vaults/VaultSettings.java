package com.group3.pwmanager.vaults;

public class VaultSettings {
    private boolean minimizeOnClose = false;

    public boolean isMinimizeOnClose () {
        return minimizeOnClose;
    }

    public void setMinimizeOnClose (boolean minimizeOnClose) {
        this.minimizeOnClose = minimizeOnClose;
    }
}