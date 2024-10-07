package org.qitter.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;

public class ConfigReader implements AutoCloseable{
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final File configPath;
    private final Properties properties;
    ConfigReader(@NotNull File configFile, @NotNull Consumer<BufferedWriter> ifFileNotExist) {
        this.configPath = configFile;
        this.properties = new Properties();

        try {
            boolean exists = configFile.exists();
            if (!exists && !configFile.createNewFile()) {
                throw Logger.getLogger().error(
                        new RuntimeException("创建" + configFile + "失败")
                );
            }
            if (!exists) {
                ifFileNotExist.accept(new BufferedWriter(new FileWriter(configFile, CHARSET)));
            }

            properties.load(new FileReader(configFile, CHARSET));
        } catch (IOException e) {
            throw Logger.getLogger().error(
                    new RuntimeException(e)
            );
        }
    }

    public Optional<String> getProperty(@NotNull String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }

    public String getProperty(@NotNull String key, @NotNull String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getInt(@NotNull String key) {
        return Integer.parseInt(
                getProperty(key).orElseThrow(()-> Logger.getLogger().error(
                        new RuntimeException("找不到配置项" + key)))
        );
    }

    public int getInt(@NotNull String key, int defaultValue) {
        if (getProperty(key).isPresent()) {
            return Integer.parseInt(getProperty(key).get());
        }
        return defaultValue;
    }

    public boolean getBoolean(@NotNull String key) {
        return Boolean.parseBoolean(
                getProperty(key).orElseThrow(()-> Logger.getLogger().error(
                        new RuntimeException("找不到配置项" + key)))
        );
    }

    public boolean getBoolean(@NotNull String key, boolean defaultValue) {
        if (getProperty(key).isPresent()) {
            return Boolean.parseBoolean(getProperty(key).get());
        }
        return defaultValue;
    }

    public double getDouble(@NotNull String key) {
        return Double.parseDouble(
                getProperty(key).orElseThrow(()-> Logger.getLogger().error(
                        new RuntimeException("找不到配置项" + key)))
        );
    }

    public double getDouble(@NotNull String key, double defaultValue) {
        if (getProperty(key).isPresent()) {
            return Double.parseDouble(getProperty(key).get());
        }
        return defaultValue;
    }

    public void setProperty(@NotNull String key, int value) {
        properties.setProperty(key, String.valueOf(value));
    }

    public void setProperty(@NotNull String key, boolean value) {
        properties.setProperty(key, String.valueOf(value));
    }

    public void setProperty(@NotNull String key, @NotNull String value) {
        properties.setProperty(key, value);
    }

    public void setProperty(@NotNull String key, double value) {
        properties.setProperty(key, String.valueOf(value));
    }

    public void save() {
        try {
            properties.store(new FileWriter(configPath,CHARSET), null);
        } catch (IOException e) {
            throw Logger.getLogger().error(
                    new RuntimeException(e)
            );
        }
    }

    public File getConfigPath() {
        return configPath;
    }

    @Contract("_, _ -> new")
    @NotNull
    public static ConfigReader create(@NotNull String configPath, @NotNull Consumer<BufferedWriter> ifFileNotExist) {
        return new ConfigReader(new File(configPath),ifFileNotExist);
    }

    @Override
    public void close() {
        save();
    }
}
