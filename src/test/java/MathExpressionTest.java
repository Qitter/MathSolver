import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.qitter.MathExpression;

import java.io.IOException;
import java.util.List;
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
    }


    @Test
    void parseTokenTest() throws IOException {
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
}
