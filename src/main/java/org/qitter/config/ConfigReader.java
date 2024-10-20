package org.qitter.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.BiConsumer;

/**
 * 配置文件读取器
 * 通过工厂方法 {@link #create(String, Map)}，来创建实例化对象，创建后的对象会被添加到 {@link #configReaders} 中，以便 {@link #closeAllConfigReaders()} 关闭所有的配置文件
 */
public class ConfigReader {
    @NotNull
    public static final Charset CHARSET = StandardCharsets.UTF_8;
    @NotNull
    private static final HashSet<ConfigReader> configReaders = new HashSet<>();
    @NotNull
    private final File configPath;
    @NotNull
    private final Properties properties;

    /**
     * 实例化一个对象，但只会被工厂方法调用
     * @param configFile 配置文件路径
     * @param ifFileNotExist 若配置文件不存在则将此键值对加载至 {@link #properties} 中
     */
    private ConfigReader(@NotNull File configFile, @NotNull Map<String,String> ifFileNotExist) {
        this.configPath = configFile;
        this.properties = new Properties();

        try {
            boolean exists = configFile.exists();
            Logger.getLogger().log(this, configFile + (exists?" 存在":" 不存在"));
            if (!exists) {
                Logger.getLogger().log(this,configFile + " 不存在，创建新文件");
                Optional.ofNullable(configFile.getParentFile()).ifPresent(p->{
                    if (!p.exists() && !p.mkdirs())
                        throw Logger.getLogger().errorAndClose(new RuntimeException("创建" + p + "失败"));
                });
                if (!configFile.createNewFile()) {
                    throw Logger.getLogger().errorAndClose(new RuntimeException("创建" + configFile + "失败"));
                }
            }
            properties.load(new FileReader(configFile, CHARSET));
            Logger.getLogger().log(this,"读取配置文件" + configFile + "成功");
            if (!exists) {
                ifFileNotExist.forEach(this::setProperty);
            }

            Logger.getLogger().log(this,properties.toString());
        } catch (IOException e) {
            throw Logger.getLogger().errorAndClose(
                    new RuntimeException(e)
            );
        }
    }

    /**
     * 遍历 {@link #properties}所有的的键与值
     * @param action 遍历的回调函数
     */
    public void forEach(@NotNull BiConsumer<String, String> action) {
        properties.forEach((a,b)-> action.accept((String) a, (String) b));
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
                getProperty(key).orElseThrow(()-> Logger.getLogger().errorAndClose(
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
                getProperty(key).orElseThrow(()-> Logger.getLogger().errorAndClose(
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
                getProperty(key).orElseThrow(()-> Logger.getLogger().errorAndClose(
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
            throw Logger.getLogger().errorAndClose(
                    new RuntimeException(e)
            );
        }
    }

    @NotNull
    public File getConfigPath() {
        return configPath;
    }

    /**
     * 创建一个配置文件读取器
     * 此方法会将创建的实例添加进 {@link #configReaders}
     * @param configPath 配置文件路径
     * @param ifFileNotExist 若配置文件不存在则将此键值对加载至 {@link #properties} 中
     * @return 配置文件读取器
     */
    @Contract("_, _ -> new")
    @NotNull
    public static ConfigReader create(@NotNull String configPath, @NotNull Map<String,String> ifFileNotExist) {
        ConfigReader configReader = new ConfigReader(new File(configPath), ifFileNotExist);
        Logger.getLogger().log("创建配置文件",configPath);
        configReaders.add(configReader);
        return configReader;
    }

    /**
     * 关闭所有的创建的配置器
     * @see #configReaders
     * @see #create(String, Map)
     */
    public static void closeAllConfigReaders() {
        getConfigReaders().forEach(ConfigReader::save);
    }

    @Contract(pure = true)
    @NotNull
    private static HashSet<ConfigReader> getConfigReaders() {
        return configReaders;
    }
}
