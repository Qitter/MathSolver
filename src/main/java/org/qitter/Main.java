package org.qitter;

import org.jetbrains.annotations.NotNull;
import org.qitter.config.ConfigEnum;
import org.qitter.config.ConfigReader;
import org.qitter.language.LanguageManager;
import org.qitter.log.Logger;
import org.qitter.ui.StageManager;
import org.qitter.ui.stage.MenuStage;

import java.util.Scanner;

public class Main {
    @NotNull
    public static final Scanner scanner = new Scanner(System.in);
    public static void main(@NotNull String[] args) {
        Logger.getLogger().setLog(
                ConfigEnum.LOG_CONFIG.getConfigReader().getBoolean("log")
        );
        LanguageManager.getInstance().setLanguage(ConfigEnum.LANGUAGE_CONFIG.getConfigReader().getProperty("language").orElse(LanguageManager.DEFAULT_LANGUAGE.getName()));
        StageManager.getInstance().pushStage(MenuStage.getInstance());
        closeAll();
    }

    public static void closeAll() {
        ConfigReader.closeAllConfigReaders();
        Logger.getLogger().close();
    }
}