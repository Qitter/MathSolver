package org.qitter.math.function;

import org.jetbrains.annotations.NotNull;
import org.qitter.math.MathExpression;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class QuadraticFunction extends MathFunction {
    @NotNull
    private static final BiFunction<MathExpression, Character, Map<Character, BigDecimal>> solveCoefficientsMethod = QuadraticFunction::solveCoefficientsMethod;
    @NotNull
    private static final MathExpression simpleForm = new MathExpression("a*x^2+b*x+c");
    @NotNull
    private static final MathExpression solveAExpression = new MathExpression("(c-2*b+a)/2");
    @NotNull
    private static final MathExpression solveBExpression = new MathExpression("y-a-c");
    @NotNull
    private static final MathExpression xExpression = new MathExpression("(-b+(b^2-4*a*(c-y))&2)/(2*a)");
    public QuadraticFunction(@NotNull String expression) {
        super(expression,simpleForm,solveCoefficientsMethod);
    }

    @NotNull
    private static Map<Character,BigDecimal> solveCoefficientsMethod(@NotNull MathExpression expression, Character variable) {
        Map<Character,BigDecimal> coefficients = new HashMap<>();
        BigDecimal y1 = expression.substituteAndWorkOut(Map.of(variable, BigDecimal.ZERO)).toMathResult().getValue();
        coefficients.put(COEFFICIENT_C,y1);
        BigDecimal y2 = expression.substituteAndWorkOut(Map.of(variable, BigDecimal.ONE)).toMathResult().getValue();
        BigDecimal y3 = expression.substituteAndWorkOut(Map.of(variable, BigDecimal.valueOf(2))).toMathResult().getValue();

        BigDecimal aValue = solveAExpression.substituteAndWorkOut(Map.of('a', y1, 'b', y2, 'c', y3)).toMathResult().getValue();
        coefficients.put(COEFFICIENT_A,aValue);

        BigDecimal bValue = solveBExpression.substituteAndWorkOut(Map.of('a', aValue, 'c', y1, 'y', y2)).toMathResult().getValue();
        coefficients.put(COEFFICIENT_B,bValue);
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
        Map<Character,MathExpression> substituteVariable = new HashMap<>();
        getCoefficients().forEach((c, v) -> substituteVariable.put(c,MathExpression.of(v)));
        substituteVariable.put(FUNCTION_RESULT,new MathExpression("" + getFunctionName()));
        substituteVariable.put(FUNCTION_VARIABLE,new MathExpression("" + getVariableName()));
        return simpleForm.substituteExpression(substituteVariable);
    }
}
