package org.qitter.ui.stage;

import org.jetbrains.annotations.NotNull;
import org.qitter.ui.StageManager;

public class EquationStage extends Stage {
    private static final EquationStage INSTANCE = new EquationStage();
    public static @NotNull EquationStage getInstance() {
        return INSTANCE;
    }

    @Override
    public void enter() {
        System.out.println("未开发，自动返回");
        StageManager.getInstance().back();
        // TODO
    }
}
