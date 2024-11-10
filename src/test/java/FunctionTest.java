import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.qitter.math.MathExpression;
import org.qitter.math.function.LinearFunction;
import org.qitter.math.function.MathFunction;
import org.qitter.math.function.QuadraticFunction;
import org.qitter.util.MathExpressionUtil;
import org.qitter.util.MathFunctionUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class FunctionTest extends BaseTest {
    @Test
    void testCreateMathFunction(){
        Class<LinearFunction> linearFunctionClass = LinearFunction.class;
        Class<? extends @NotNull MathFunction> aClass = MathFunctionUtil.createMathFunction(new MathExpression("2*x+8")).getClass();
        assertEquals(linearFunctionClass,aClass);

        Class<QuadraticFunction> quadraticFunctionClass = QuadraticFunction.class;
        aClass = MathFunctionUtil.createMathFunction(new MathExpression("x^2+8")).getClass();
        assertEquals(quadraticFunctionClass,aClass);

        assertThrowsExactly(TimeoutException.class,()-> MathExpressionUtil.getFunctionTimes(new MathExpression("x&2"), 1, TimeUnit.MILLISECONDS));
    }
}
