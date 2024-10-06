package org.qitter.ui;

import org.jetbrains.annotations.NotNull;
import org.qitter.ui.stage.Stage;

import java.util.ArrayDeque;
import java.util.Optional;

public class StageManager {
    private static final StageManager INSTANCE = new StageManager();
    private StageManager() {}
    public static StageManager getInstance() {
        return INSTANCE;
    }
    private Stage currentStage;
    private final ArrayDeque<Stage> previousStages = new ArrayDeque<>();
    public void pushStage(@NotNull Stage stage) {
        Optional.ofNullable(currentStage).ifPresent(Stage::exit);
        currentStage = stage;
        previousStages.push(currentStage);
        currentStage.enter();
    }

    public void back() {
        Optional.ofNullable(currentStage).ifPresent(Stage::exit);
        previousStages.pop();
        Optional.ofNullable(previousStages.peek()).ifPresent(stage -> {
            currentStage = stage;
            stage.enter();
        });
    }
}
