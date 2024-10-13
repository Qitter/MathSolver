package org.qitter.config;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public enum ConfigEnum {
    LOG_CONFIG("log.properties",
            Map.of(
                    "log", String.valueOf(true)
            )
    ),
    LANGUAGE_CONFIG("language.properties",
            Map.of("language","chinese")
    );
    @NotNull
    private final ConfigReader configReader;
    ConfigEnum(@NotNull String configPath, @NotNull Map<String,String> ifFileNotExist) {
        this.configReader = ConfigReader.create(configPath,ifFileNotExist);
    }

    @NotNull
    public ConfigReader getConfigReader() {
        return configReader;
    }
}
