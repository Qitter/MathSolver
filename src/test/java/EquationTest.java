import org.junit.jupiter.api.Test;
import org.qitter.math.equation.LinearEquation;
import org.qitter.math.equation.MathEquation;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class EquationTest extends BaseTest{
    @Test
    void listEquations() {
        assertThrowsExactly(IllegalArgumentException.class,()-> new LinearEquation("8=x^2+b"));
        MathEquation equation = new LinearEquation("8=2*x+2*x");
        assertEquals(equation.solve().getValue(), BigDecimal.valueOf(2));
    }
}
