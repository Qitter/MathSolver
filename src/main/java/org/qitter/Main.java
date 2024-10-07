package org.qitter;

import org.jetbrains.annotations.NotNull;
import org.qitter.config.ConfigReaderEnum;
import org.qitter.log.Logger;
import org.qitter.ui.StageManager;
import org.qitter.ui.stage.MenuStage;

import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);
    public static void main(@NotNull String[] args) {
        Logger.getLogger().setLog(
                ConfigReaderEnum.LOG_CONFIG.getConfigReader().getBoolean("log")
        );
        StageManager.getInstance().pushStage(MenuStage.getInstance());
//        Logger.getLogger().close();
    }
}