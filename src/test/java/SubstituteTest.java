import org.junit.jupiter.api.Test;
import org.qitter.math.MathExpression;
import org.qitter.math.function.LinearFunction;
import org.qitter.math.function.MathFunction;
import org.qitter.math.function.QuadraticFunction;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SubstituteTest extends BaseTest{
    @Test
    void substituteTest(){
        MathExpression add = new MathExpression("a+b");
        MathExpression substitute = add.substituteAndWorkOut(Map.of('a', BigDecimal.valueOf(1), 'b', BigDecimal.valueOf(2)));
        assertEquals(substitute.getExpression(),"3");

        assertThrows(NumberFormatException.class,()-> add.substituteAndWorkOut(Map.of('a', BigDecimal.valueOf(1))));
    }

    @Test
    void fxTest(){
//        assertThrowsExactly(IllegalArgumentException.class,()-> new LinearFunction("x"));
        MathFunction function = new LinearFunction("y=2*x+4+4");
        assertEquals(function.getExpression().getExpression(),"2*x+4+4");
        assertEquals(function.getFunctionName(),'y');
        assertEquals(function.getVariableName(),'x');
        assertEquals(function.getCoefficients(),Map.of('a', BigDecimal.valueOf(2),'b',BigDecimal.valueOf(8)));

        assertEquals(function.getY(BigDecimal.valueOf(0)),BigDecimal.valueOf(8));
        assertEquals(function.getY(BigDecimal.valueOf(1)),BigDecimal.valueOf(10));
        assertEquals(function.getY(BigDecimal.valueOf(2)),BigDecimal.valueOf(12));
        assertEquals(function.getY(BigDecimal.valueOf(3)),BigDecimal.valueOf(14));
        assertEquals(function.getY(BigDecimal.valueOf(4)),BigDecimal.valueOf(16));
        assertEquals(function.getY(BigDecimal.valueOf(5)),BigDecimal.valueOf(18));

        assertEquals(function.getX(BigDecimal.valueOf(0)),BigDecimal.valueOf(-4));
        assertEquals(function.getX(BigDecimal.valueOf(1)),BigDecimal.valueOf(-3.5));
        assertEquals(function.getX(BigDecimal.valueOf(2)),BigDecimal.valueOf(-3));
        assertEquals(function.getX(BigDecimal.valueOf(3)),BigDecimal.valueOf(-2.5));
        assertEquals(function.getX(BigDecimal.valueOf(4)),BigDecimal.valueOf(-2));
        assertEquals(function.getX(BigDecimal.valueOf(5)),BigDecimal.valueOf(-1.5));

        assertEquals(function.asSimpleForm().getExpression(),"2*x+8");

        QuadraticFunction quadraticFunction = new QuadraticFunction("y=2*x^2+4*x+4");
        assertEquals(quadraticFunction.getExpression().getExpression(),"2*x^2+4*x+4");
        assertEquals(quadraticFunction.getFunctionName(),'y');
        assertEquals(quadraticFunction.getVariableName(),'x');
        assertEquals(quadraticFunction.getCoefficients(),Map.of('a', BigDecimal.valueOf(2),'b',BigDecimal.valueOf(4),'c',BigDecimal.valueOf(4)));
        assertEquals(quadraticFunction.getY(BigDecimal.valueOf(0)),BigDecimal.valueOf(4));
        assertEquals(quadraticFunction.getY(BigDecimal.valueOf(1)),BigDecimal.valueOf(10));
        assertEquals(quadraticFunction.getY(BigDecimal.valueOf(2)),BigDecimal.valueOf(20));
        assertEquals(quadraticFunction.getY(BigDecimal.valueOf(3)),BigDecimal.valueOf(34));
        assertEquals(quadraticFunction.getY(BigDecimal.valueOf(4)),BigDecimal.valueOf(52));
        assertEquals(quadraticFunction.getY(BigDecimal.valueOf(5)),BigDecimal.valueOf(74));


        function = new LinearFunction("w=2*f");
        assertEquals(function.getExpression().getExpression(),"2*f");
        assertEquals(function.getFunctionName(),'w');
        assertEquals(function.getVariableName(),'f');
        assertEquals(function.getCoefficients(),Map.of('a', BigDecimal.valueOf(2),'b',BigDecimal.valueOf(0)));
        assertEquals(function.getY(BigDecimal.valueOf(0)),BigDecimal.valueOf(0));
        assertEquals(function.getY(BigDecimal.valueOf(1)),BigDecimal.valueOf(2));
        assertEquals(function.getY(BigDecimal.valueOf(2)),BigDecimal.valueOf(4));
        assertEquals(function.getY(BigDecimal.valueOf(3)),BigDecimal.valueOf(6));
        assertEquals(function.getY(BigDecimal.valueOf(4)),BigDecimal.valueOf(8));
        assertEquals(function.getY(BigDecimal.valueOf(5)),BigDecimal.valueOf(10));
    }
}
