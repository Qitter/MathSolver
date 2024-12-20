package org.qitter.math.function;

import org.jetbrains.annotations.NotNull;
import org.qitter.math.MathExpression;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class LinearFunction extends MathFunction {
    @NotNull
    private static final BiFunction<MathExpression, Character, Map<Character, BigDecimal>> solveCoefficientsMethod = LinearFunction::solveCoefficientsMethod;
    @NotNull
    private static final MathExpression simpleForm = new MathExpression("a*x+b");
    @NotNull
    private static final MathExpression xExpression = new MathExpression("(y-b)/a");
    public LinearFunction(@NotNull String expression){
        super(expression, simpleForm, solveCoefficientsMethod);
    }

    @NotNull
    private static Map<Character, BigDecimal> solveCoefficientsMethod(@NotNull MathExpression expression, char variableName){
        Map<Character,BigDecimal> coefficients = new HashMap<>();

        BigDecimal valueB = expression.substituteAndWorkOut(Map.of(variableName, BigDecimal.ZERO)).toMathResult().getValue();
        BigDecimal valueA = expression.substituteAndWorkOut(Map.of(variableName, BigDecimal.ONE)).toMathResult().getValue().subtract(valueB);
        coefficients.put(COEFFICIENT_A, valueA);
        coefficients.put(COEFFICIENT_B, valueB);

        return coefficients;
    }

    @Override
    public @NotNull BigDecimal getY(@NotNull BigDecimal x) {
        Map<Character,BigDecimal> var = new HashMap<>(Map.copyOf(getCoefficients()));
        var.put(FUNCTION_VARIABLE,x);
        return simpleForm.substituteAndWorkOut(var).toMathResult().getValue();
    }

    @Override
    public @NotNull BigDecimal getX(@NotNull BigDecimal y) {
        Map<Character,BigDecimal> var = new HashMap<>(Map.copyOf(getCoefficients()));
        var.put(FUNCTION_RESULT,y);
        return xExpression.substituteAndWorkOut(var).toMathResult().getValue();
    }

    @Override
    public @NotNull MathExpression asSimpleForm() {
        return simplyExpression();
    }
}
