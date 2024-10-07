package org.qitter.config;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public enum ConfigReaderEnum {
    LOG_CONFIG("log.properties",(writer -> {
        try {
            writer.write("""
                    log=true
                    """);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }));
    private final ConfigReader configReader;
    ConfigReaderEnum(@NotNull String configPath, @NotNull Consumer<BufferedWriter> ifFileNotExist) {
        this.configReader = ConfigReader.create(configPath,ifFileNotExist);
    }

    public ConfigReader getConfigReader() {
        return configReader;
    }
}
