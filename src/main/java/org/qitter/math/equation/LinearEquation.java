package org.qitter.math.equation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.qitter.math.MathExpression;
import org.qitter.math.MathResult;
import org.qitter.math.function.LinearFunction;
import org.qitter.math.function.MathFunction;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class LinearEquation extends MathEquation {
    @NotNull
    private static final Function<String,MathFunction> parseFunctionMethod = s-> new LinearFunction("y=" + s);
    /**
     * ax + b = cx + d
     * ax - cx = d - b
     * (a - c)x = d - b
     * x = (d - b) / (a - c)
     */
    @NotNull
    private static final MathExpression solveXExpression = new MathExpression("(d-b)/(a-c)");
    public LinearEquation(@NotNull String expression) {
        super(expression, parseFunctionMethod);
    }
    @Nullable
    private MathResult result;
    @Override
    public @NotNull MathResult solve() {
        return Optional.ofNullable(result).orElseGet(()->{
            BigDecimal leftA = getLeft().getCoefficient(MathFunction.COEFFICIENT_A);
            BigDecimal leftB = getLeft().getCoefficient(MathFunction.COEFFICIENT_B);
            BigDecimal rightA = getRight().getCoefficient(MathFunction.COEFFICIENT_A);
            BigDecimal rightB = getRight().getCoefficient(MathFunction.COEFFICIENT_B);
            return result = solveXExpression.substituteAndWorkOut(Map.of(
            MathFunction.COEFFICIENT_A, leftA,MathFunction.COEFFICIENT_B, leftB,MathFunction.COEFFICIENT_C, rightA,MathFunction.COEFFICIENT_D, rightB
            )).toMathResult();
        });
    }
}
