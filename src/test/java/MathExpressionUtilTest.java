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
            System.out.println(MathExpressionUtil.getFunctionTimes(new MathExpression("x&2"), 1, TimeUnit.SECONDS));
        } catch (ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSimplify() {
        MathExpression mathExpression = new MathExpression("x*(100-(x-3000)/50)-100*((x-3000)/50)");
        System.out.println(MathExpressionUtil.simplify(mathExpression));
    }
}
