import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qitter.math.MathExpression;
import org.qitter.math.MathOperator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MathExpressionTest extends BaseTest{
    @Test
    void run() {
        String mult = MathExpression.workOutToken("2*3");
        assertEquals(mult,String.valueOf(2*3));
        String div = MathExpression.workOutToken("6/3");
        assertEquals(div,String.valueOf(6/3));
        String sub = MathExpression.workOutToken("-6+3");
        assertEquals(sub,String.valueOf(-6+3));
        String pow = MathExpression.workOutToken("2^3");
        assertEquals(pow,String.valueOf((int)Math.pow(2,3)));
        String multDiv = MathExpression.workOutToken("2*3/2");
        assertEquals(multDiv,String.valueOf(2*3/2));
        String multDivSub = MathExpression.workOutToken("2*3/2-1");
        assertEquals(multDivSub,String.valueOf(2*3/2-1));
        String multDivSubPow = MathExpression.workOutToken("2*3/2-1^2");
        // 6 / 2 - 1 = 3 - 1 = 2
        assertEquals(multDivSubPow,String.valueOf(3-1));

        MathExpression mathExpression = new MathExpression("(-1+2)*(3+7)");
        mathExpression.workOut();
        assertEquals(mathExpression.getExpression(),"10");

        BigDecimal log = MathOperator.ROOT.apply(BigDecimal.valueOf(4),BigDecimal.valueOf(2));
        assertEquals(log,BigDecimal.valueOf(2));
    }


    @Test
    void parseTokenTest() {
        Result result = getResult("test/parseToken.txt");
        List<String> inputs = result.inputs();
        List<String> expected = result.expected();
        for (int i = 0; i < inputs.size(); i++) {
            String input = inputs.get(i);
            String expectedString = expected.get(i);
            List<String> tokens = MathExpression.parseTokens(input);
            StringJoiner sj = new StringJoiner(",","[","]");
            for (String token : tokens) {
                sj.add(token);
            }
            Assertions.assertEquals(expectedString,sj.toString());
        }
    }

    @Test
    void variableTest() {
        MathExpression xExp = new MathExpression("x+2");
        Assertions.assertEquals(xExp.getVariables(), Set.of('x'));

        MathExpression yExp = new MathExpression("y+2");
        Assertions.assertEquals(yExp.getVariables(), Set.of('y'));

        MathExpression xyExp = new MathExpression("x+y");
        Assertions.assertEquals(xyExp.getVariables(), Set.of('x','y'));

        MathExpression x2yExp = new MathExpression("x^2+y");
        Assertions.assertEquals(x2yExp.getVariables(), Set.of('x','y'));
    }
}
