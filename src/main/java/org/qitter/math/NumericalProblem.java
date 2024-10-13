package org.qitter.math;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;

import java.util.Optional;

/**
 * 无未知数的数学表达式
 */
public class NumericalProblem extends MathProblem {
    public NumericalProblem(@NotNull String expression) {
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
