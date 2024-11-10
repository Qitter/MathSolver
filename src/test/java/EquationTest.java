import org.junit.jupiter.api.Test;
import org.qitter.math.function.NotMathFunctionException;
import org.qitter.math.function.QuadraticFunction;
import org.qitter.math.problems.AlgebraProblem;
import org.qitter.math.problems.MathProblem;
import org.qitter.math.problems.NumericalProblem;
import org.qitter.math.problems.equation.LinearEquation;
import org.qitter.math.problems.MathEquation;
import org.qitter.math.problems.equation.QuadraticEquation;
import org.qitter.util.MathProblemUtil;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class EquationTest extends BaseTest{
    @Test
    void listEquations() {
        Class<NumericalProblem> numericalProblemClass = NumericalProblem.class;
        Class<LinearEquation> linearEquation = LinearEquation.class;
        Class<QuadraticEquation> quadraticEquation = QuadraticEquation.class;
        Class<AlgebraProblem> algebraProblemClass = AlgebraProblem.class;

        MathProblem mathProblem = MathProblemUtil.createMathProblem("2+8+60+90");
        assertEquals(mathProblem.getClass(),numericalProblemClass);
        assertEquals(mathProblem.solve().iterator().next().toString(),"160");

        mathProblem = MathProblemUtil.createMathProblem("2*x+8*x+60*x+90*x=50");
        assertEquals(mathProblem.getClass(),linearEquation);
        assertEquals("0.3125",mathProblem.solve().iterator().next().toString());

        mathProblem = MathProblemUtil.createMathProblem("x^2+x*4-80=4");
        assertEquals(mathProblem.getClass(),quadraticEquation);
        System.out.println(mathProblem.solve());

        mathProblem = MathProblemUtil.createMathProblem("x^2+x*4+6+4");
        assertEquals(mathProblem.getClass(),algebraProblemClass);
    }
}
