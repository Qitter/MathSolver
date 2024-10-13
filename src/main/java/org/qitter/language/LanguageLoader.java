package org.qitter.language;

import org.jetbrains.annotations.NotNull;
import org.qitter.config.ConfigReader;
import org.qitter.log.Logger;

import java.util.Map;

public class LanguageLoader {
    @NotNull
    private static final String LANGUAGE_FILE_STORE_PATH = "language/";
    @NotNull
    private static final String LANGUAGE_FILE_SUFFIX = ".lang";
    @NotNull
    private final ConfigReader configReader;
    public LanguageLoader(@NotNull Language language,@NotNull Map<String,String> ifFileNotExist) {
        configReader = ConfigReader.create(LANGUAGE_FILE_STORE_PATH + language.getName() + LANGUAGE_FILE_SUFFIX, ifFileNotExist);
    }

    @NotNull
    public String getString(@NotNull String key) {
        Logger.getLogger().log(this, "getString方法 " + "key: " + key);
        return configReader.getProperty(key).orElse("");
    }

    @NotNull
    public String getString(@NotNull String key, @NotNull String defaultValue) {
        return configReader.getProperty(key, defaultValue);
    }
}
