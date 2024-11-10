package org.qitter.math.problems.equation;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.math.MathExpression;
import org.qitter.math.MathResult;
import org.qitter.math.function.MathFunction;
import org.qitter.math.function.QuadraticFunction;
import org.qitter.math.problems.MathEquation;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class QuadraticEquation extends MathEquation {
    private static final @NotNull Function<String, MathFunction> parseFunctionMethod = QuadraticFunction::new;
    private static final @NotNull MathExpression deltaExpression = new MathExpression("((b-e)^2-(4*(a-d)*(c-f)))&2");
    private static final @NotNull MathExpression solveExpression1 = new MathExpression("((-b+e)+g)/2*(a-d)");
    private static final @NotNull MathExpression solveExpression2 = new MathExpression("((-b+e)-g)/2*(a-d)");
    public QuadraticEquation(@NotNull String expression) {
        super(expression, parseFunctionMethod);
    }

    @Override
    public @NotNull Set<MathResult> solve() {
        Logger.getLogger().log(this,"正在求解... " + expression);
        Set<MathResult> results = new HashSet<>();
        BigDecimal leftA = getLeft().getCoefficient(MathFunction.COEFFICIENT_A);
        BigDecimal leftB = getLeft().getCoefficient(MathFunction.COEFFICIENT_B);
        BigDecimal leftC = getLeft().getCoefficient(MathFunction.COEFFICIENT_C);

        BigDecimal rightA = getRight().getCoefficient(MathFunction.COEFFICIENT_A);
        BigDecimal rightB = getRight().getCoefficient(MathFunction.COEFFICIENT_B);
        BigDecimal rightC = getRight().getCoefficient(MathFunction.COEFFICIENT_C);

        BigDecimal delta = deltaExpression.substituteAndWorkOut(Map.of(
                MathFunction.COEFFICIENT_A,leftA,MathFunction.COEFFICIENT_B,leftB,MathFunction.COEFFICIENT_C,leftC,
                MathFunction.COEFFICIENT_D,rightA,MathFunction.COEFFICIENT_E,rightB,MathFunction.COEFFICIENT_F,rightC
        )).toMathResult().getValue();

        Logger.getLogger().log(expression,"delta = " + delta);
        if (delta.compareTo(BigDecimal.ZERO) > 0) {
            results.add(solveExpression1.substituteAndWorkOut(Map.of(
                    MathFunction.COEFFICIENT_A,leftA,MathFunction.COEFFICIENT_B,leftB,
                    MathFunction.COEFFICIENT_D,rightA, MathFunction.COEFFICIENT_E,rightB,
                    MathFunction.COEFFICIENT_G,delta
            )).toMathResult());

            results.add(solveExpression2.substituteAndWorkOut(Map.of(
                    MathFunction.COEFFICIENT_A,leftA,MathFunction.COEFFICIENT_B,leftB,
                    MathFunction.COEFFICIENT_D,rightA, MathFunction.COEFFICIENT_E,rightB,
                    MathFunction.COEFFICIENT_G,delta
            )).toMathResult());
        }
        Logger.getLogger().log(expression,"results = " + results);
        return results;
    }
}
