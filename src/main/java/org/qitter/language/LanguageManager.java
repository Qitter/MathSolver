package org.qitter.language;

import org.jetbrains.annotations.NotNull;

public class LanguageManager {
    @NotNull
    public static final Language DEFAULT_LANGUAGE = Language.CHINESE;
    @NotNull
    private static final LanguageManager instance = new LanguageManager();
    @NotNull
    public static LanguageManager getInstance() {
        return instance;
    }
    @NotNull
    private Language currentLanguage = DEFAULT_LANGUAGE;
    private LanguageManager() {}

    public void setLanguage(@NotNull Language language) {
        this.currentLanguage = language;
    }

    public void setLanguage(@NotNull String language) {
        this.currentLanguage = Language.valueOf(language.toUpperCase());
    }

    @NotNull
    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    @NotNull
    public String getString(String key) {
        return getCurrentLanguage().getLanguageLoader().getString(key);
    }
}
