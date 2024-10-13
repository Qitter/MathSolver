package org.qitter.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ConfigReader {
    @NotNull
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    @NotNull
    private static final HashSet<ConfigReader> configReaders = new HashSet<>();
    @NotNull
    private final File configPath;
    @NotNull
    private final Properties properties;
    ConfigReader(@NotNull File configFile, @NotNull Map<String,String> ifFileNotExist) {
        this.configPath = configFile;
        this.properties = new Properties();

        try {
            boolean exists = configFile.exists();

            Logger.getLogger().log(this, configFile + (exists?" 存在":" 不存在"));
            if (!exists) {
                Logger.getLogger().log(this,configFile + " 不存在，创建新文件");
                Optional.ofNullable(configFile.getParentFile()).ifPresent(p->{
                    if (!p.exists() && !p.mkdirs())
                        throw Logger.getLogger().error(new RuntimeException("创建" + p + "失败"));
                });
                if (!configFile.createNewFile()) {
                    throw Logger.getLogger().error(new RuntimeException("创建" + configFile + "失败"));
                }
            }

            properties.load(new FileReader(configFile, CHARSET));
            Logger.getLogger().log(this,"读取配置文件" + configFile + "成功");
            if (!exists) {
                ifFileNotExist.forEach(this::setProperty);
            }

            Logger.getLogger().log(this,properties.toString());
        } catch (IOException e) {
            throw Logger.getLogger().error(
                    new RuntimeException(e)
            );
        }
    }

    @NotNull
    public Optional<String> getProperty(@NotNull String key) {
        return Optional.ofNullable(properties.getProperty(key));
    }

    @NotNull
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
        Logger.getLogger().log(this,"正在保存",properties.toString());
        try {
            properties.store(new FileWriter(configPath,CHARSET), null);
        } catch (IOException e) {
            throw Logger.getLogger().error(
                    new RuntimeException(e)
            );
        }
    }

    @NotNull
    public File getConfigPath() {
        return configPath;
    }

    @Contract("_, _ -> new")
    @NotNull
    public static ConfigReader create(@NotNull String configPath, @NotNull Map<String,String> ifFileNotExist) {
        ConfigReader configReader = new ConfigReader(new File(configPath), ifFileNotExist);
        Logger.getLogger().log("创建配置文件",configPath);
        configReaders.add(configReader);
        return configReader;
    }

    public static void closeAllConfigReaders() {
        getConfigReaders().forEach(ConfigReader::save);
    }

    @NotNull
    private static HashSet<ConfigReader> getConfigReaders() {
        return configReaders;
    }
}
