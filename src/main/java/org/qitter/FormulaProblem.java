package org.qitter;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;

import java.util.Optional;

public class FormulaProblem extends MathProblem{
    public FormulaProblem(@NotNull String expression) {
        super(expression);
        Logger.getLogger().log(this,"已创建");
    }

    @Override
    public @NotNull MathResult solve() {
        Logger.getLogger().log(this,"进入 solve 方法");
        return Optional.ofNullable(result)
                .orElseGet(() -> result = MathResult.ofNumber(expression.workOut()));
    }
}
