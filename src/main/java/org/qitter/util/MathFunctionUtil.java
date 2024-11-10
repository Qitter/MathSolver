package org.qitter.util;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.math.MathExpression;
import org.qitter.math.function.LinearFunction;
import org.qitter.math.function.MathFunction;
import org.qitter.math.function.QuadraticFunction;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class MathFunctionUtil {
    public static final int
            LINER_FUNCTION = 1,
            QUADRATIC_FUNCTION = 2;
    @NotNull
    private static final Map<Integer, Function<String, MathFunction>> TIMES_OF_SUBCLASSES =
            Map.of(
                    LINER_FUNCTION, LinearFunction::new,
                    QUADRATIC_FUNCTION, QuadraticFunction::new
            );

    @NotNull
    public static MathFunction createMathFunction(@NotNull MathExpression expression){
        return Optional.ofNullable(TIMES_OF_SUBCLASSES.get(MathExpressionUtil.getFunctionTimes(expression))).orElseThrow(
                () -> Logger.getLogger().errorAndClose(new CannotParseMathFunctionException(expression.getExpression() + " 无法解析的函数"))
        ).apply(expression.getExpression());
    }

    public static class CannotParseMathFunctionException extends RuntimeException {
        public CannotParseMathFunctionException(String message) {
            super(message);
        }
    }
}
