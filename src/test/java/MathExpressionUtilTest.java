import org.junit.jupiter.api.Test;
import org.qitter.math.MathExpression;
import org.qitter.util.BigDecimalUtil;
import org.qitter.util.MathExpressionUtil;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathExpressionUtilTest extends BaseTest {
    @Test
    void testGetFunctionTimes() {
        int functionTimes = MathExpressionUtil.getFunctionTimes(new MathExpression("x+8"));
        assertEquals(1,functionTimes);

        functionTimes = MathExpressionUtil.getFunctionTimes(new MathExpression("x^2+8"));
        assertEquals(2,functionTimes);

        functionTimes = MathExpressionUtil.getFunctionTimes(new MathExpression("x^3"));
        assertEquals(3,functionTimes);


        functionTimes = MathExpressionUtil.getFunctionTimes(new MathExpression("x^4"));
        assertEquals(4,functionTimes);

        functionTimes = MathExpressionUtil.getFunctionTimes(new MathExpression("x^5"));
        assertEquals(5,functionTimes);

        functionTimes = MathExpressionUtil.getFunctionTimes(new MathExpression("x^6"));
        assertEquals(6,functionTimes);

        try {
            System.out.println(MathExpressionUtil.getFunctionTimes(new MathExpression("x&2"), 10, TimeUnit.SECONDS));
        } catch (ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSimplify() {
        assertEquals(new BigDecimal("0.00").stripTrailingZeros(),BigDecimal.ZERO);
        test(s-> MathExpressionUtil.simplify(new MathExpression(s)).getExpression(),getResult("test/simplifyExpressions.txt"));
    }
}
