package org.qitter.math.problems;

import org.jetbrains.annotations.NotNull;
import org.qitter.math.MathExpression;
import org.qitter.math.MathResult;
import org.qitter.util.MathExpressionUtil;

import java.util.Set;

public class AlgebraProblem extends MathProblem {
    public AlgebraProblem(@NotNull String expression) {
        super(expression);
    }

    public AlgebraProblem(@NotNull MathExpression expression) {
        super(expression);
    }

    @Override
    public @NotNull Set<MathResult> solve() {
        return Set.of(
                MathExpressionUtil.simplify(getExpression()).toMathResult()
        );
    }
}
