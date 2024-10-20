package org.qitter.math.equation;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.math.MathProblem;
import org.qitter.math.function.MathFunction;

import java.util.function.Function;

public abstract class MathEquation extends MathProblem {
    @NotNull
    private final MathFunction left;
    @NotNull
    private final MathFunction right;
    protected MathEquation(
            @NotNull String expression,
            @NotNull Function<String,MathFunction> parseFunctionMethod) {
        super(expression);
        int index = expression.indexOf('=');
        if (index == -1) {
            throw Logger.getLogger().errorAndClose(
                    new IllegalArgumentException("方程必须含有等号 : " + expression)
            );
        }
        String leftExpression = expression.substring(0, index);
        String rightExpression = expression.substring(index + 1);
        this.left = parseFunctionMethod.apply(leftExpression);
        this.right = parseFunctionMethod.apply(rightExpression);
    }

    @NotNull
    public MathFunction getLeft() {
        return left;
    }

    @NotNull
    public MathFunction getRight() {
        return right;
    }
}
