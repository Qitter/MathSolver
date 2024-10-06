package org.qitter.ui.stage;

import org.jetbrains.annotations.NotNull;
import org.qitter.ui.StageManager;

public class AlgebraStage extends Stage{
    public static final AlgebraStage INSTANCE = new AlgebraStage();

    @NotNull
    public static AlgebraStage getInstance() {
        return INSTANCE;
    }
    private AlgebraStage() {}
    @Override
    public void enter() {
        System.out.println("未开发,自动返回");
        StageManager.getInstance().back();
        // TODO
    }

    @Override
    public void exit() {
        // TODO
    }
}
