package org.qitter.ui.stage;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;

public abstract class SwitchStage extends Stage {
    @NotNull
    private final Map<String,Runnable> runnableMap;
    @NotNull
    private final Runnable DEFAULT_RUNNABLE;
    protected SwitchStage (@NotNull Map<String,Runnable> runnableMap,@NotNull Runnable defaultRunnable) {
        this.runnableMap = runnableMap;
        this.DEFAULT_RUNNABLE = defaultRunnable;
    }
    protected SwitchStage (@NotNull Map<String,Runnable> runnableMap) {
        this(runnableMap,() -> {});
    }

    @NotNull
    protected Optional<Runnable> getRunnable (@NotNull String key) {
        return Optional.ofNullable(runnableMap.get(key));
    }

    protected void applyRunnable(@NotNull String key) {
        getRunnable(key).ifPresentOrElse(Runnable::run, DEFAULT_RUNNABLE);
    }
}
