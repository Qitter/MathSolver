package org.qitter.util;

import org.jetbrains.annotations.NotNull;
import org.qitter.math.MathExpression;
import org.qitter.math.problems.AlgebraProblem;
import org.qitter.math.problems.MathProblem;
import org.qitter.math.problems.NumericalProblem;

import java.util.Map;
import java.util.function.Function;

public class MathProblemUtil {
    public enum ProblemType {
        ALGEBRA,
        NUMERICAL,
        EQUATION
    }
    private static final @NotNull Map<ProblemType, Function<String, MathProblem>> PROBLEM_TYPE_MAP = Map.of(
            ProblemType.ALGEBRA, AlgebraProblem::new,
            ProblemType.NUMERICAL, NumericalProblem::new,
            ProblemType.EQUATION, MathEquationUtil::createMathEquation
    );
    @NotNull
    public static MathProblem createMathProblem(@NotNull String expression) {
        return PROBLEM_TYPE_MAP.get(MathProblemUtil.parseProblemType(expression)).apply(expression);
    }

    @NotNull
    public static ProblemType parseProblemType(@NotNull String expression) {
        if(MathEquationUtil.isEquation(expression)) {
            return ProblemType.EQUATION;
        }
        MathExpression mathExpression = new MathExpression(expression);
        if(mathExpression.hasVariable()) {
            return ProblemType.ALGEBRA;
        }
        return ProblemType.NUMERICAL;
    }
}
