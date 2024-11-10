package org.qitter.util;

import org.jetbrains.annotations.NotNull;
import org.qitter.math.problems.MathEquation;
import org.qitter.math.problems.equation.LinearEquation;
import org.qitter.math.problems.equation.QuadraticEquation;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class MathEquationUtil {
    private static final Map<Integer, Function<String,MathEquation>> TIMES_OF_EQUATION = Map.of(
            1, LinearEquation::new,
            2, QuadraticEquation::new
    );
    @NotNull
    public static MathEquation createMathEquation(@NotNull String expression) {
        int times = Arrays.stream(expression.split("=")).map(MathExpressionUtil::getFunctionTimes).max(Integer::compareTo).orElseThrow();
        return Optional.ofNullable(TIMES_OF_EQUATION.get(times)).orElseThrow().apply(expression);
    }

    public static boolean isEquation(@NotNull String expression) {
        return expression.indexOf('=') != -1;
    }
}
