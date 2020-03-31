package com.libanminds.repositories;

import com.libanminds.models.Settings;

public class SettingsRepository {
    public static Settings fetchCurrentSettings() {
        try {
            //TODO fetch from DB
            return new Settings();
        } catch (Exception e) {
            e.printStackTrace();
            return new Settings();  // Fallback settings
        }
    }

    private SettingsRepository() {
        throw new AssertionError("You are not allowed to instantiate this class");
    }
}
