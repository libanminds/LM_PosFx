package com.libanminds.singletons;

import com.libanminds.models.Settings;
import com.libanminds.repositories.SettingsRepository;

public class GlobalSettings {
    private static GlobalSettings instance;
    public Settings settings;

    public static Settings fetch() {
        return getInstance().settings;
    }

    public static GlobalSettings getInstance() {
        if (instance == null) instance = new GlobalSettings();
        return instance;
    }

    private GlobalSettings() {
        settings = SettingsRepository.fetchCurrentSettings();
    }
}
