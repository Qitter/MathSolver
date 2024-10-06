package org.qitter;

import org.jetbrains.annotations.NotNull;

public abstract class MathProblem {
    public MathProblem(@NotNull String expression) {
        this.expression = new MathExpression(expression.trim());
    }
    @NotNull
    protected final MathExpression expression;
    protected MathResult result;
    @NotNull
    public abstract MathResult solve();
    @NotNull
    public MathExpression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return super.toString()+"["+ expression + "]";
    }
}
