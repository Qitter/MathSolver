package org.qitter.math.problems;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.math.MathExpression;
import org.qitter.math.MathResult;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * 无未知数的数学表达式
 */
public class NumericalProblem extends MathProblem {
    public NumericalProblem(@NotNull String expression) {
        super(expression);
        Logger.getLogger().log(this,"已创建");
    }

    public NumericalProblem(@NotNull MathExpression expression) {
        super(expression);
    }

    @Override
    public @NotNull Set<MathResult> solve() {
        Logger.getLogger().log(this,"进入 solve 方法");
        return Collections.singleton(Optional.ofNullable(result)
                .orElseGet(() -> result = MathResult.ofNumber(expression.workOut().toString())));
    }
}
