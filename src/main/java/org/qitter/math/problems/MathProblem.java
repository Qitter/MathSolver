package org.qitter.math.problems;

import org.jetbrains.annotations.NotNull;
import org.qitter.math.MathExpression;
import org.qitter.math.MathResult;

import java.util.Set;

public abstract class MathProblem {
    public MathProblem(@NotNull String expression) {
        this.expression = new MathExpression(expression.trim());
    }
    public MathProblem(@NotNull MathExpression expression) {
        this.expression = expression;
    }
    @NotNull
    protected final MathExpression expression;
    protected MathResult result;
    public abstract @NotNull Set<MathResult> solve();
    @NotNull
    public MathExpression getExpression() {
        return expression;
    }

    @Override
    @NotNull
    public String toString() {
        return super.toString()+"["+ expression + "]";
    }
}
