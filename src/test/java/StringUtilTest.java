import org.junit.jupiter.api.Test;
import org.qitter.math.MathExpression;
import org.qitter.util.MathExpressionUtil;
import org.qitter.util.StringUtil;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilTest extends BaseTest{
    @Test
    void testExtractBracket() {
        // TODO
    }

    @Test
    void testIsCloseBracket() {
        String name = "test/isCloseBracket.txt";
        Result result = getResult(name);
        test(s-> Boolean.toString(StringUtil.isCloseBracket(s)),result);
    }

    @Test
    void testExitsCloseBracket() {
        String name = "test/exitsCloseBracket.txt";
        Result result = getResult(name);
        test(s-> Boolean.toString(StringUtil.exitsCloseBracket(s)),result);
    }
    @Test
    void testFormatNumber() {
        String name = "test/removeNegative.txt";
        Result result = getResult(name);
//        for (int i = 0; i < result.inputs().size(); i++) {
//            String expected = result.expected().get(i);
//            String removeNegative = StringUtil.formatNumber(result.inputs().get(i));
//            assertEquals(expected, removeNegative);
//        }
        test(StringUtil::formatNumber,result);
    }

    @Test
    void testGetFunctionTime() {

    }
}
