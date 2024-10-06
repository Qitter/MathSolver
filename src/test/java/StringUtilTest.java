import org.junit.jupiter.api.Test;
import org.qitter.util.StringUtil;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilTest extends BaseTest{
    @Test
    void testExtractBracket() {
        // TODO
    }

    @Test
    void testIsCloseBracket() throws IOException {
        String name = "test/isCloseBracket.txt";
        Result result = getResult(name);
        for (int i = 0; i < result.inputs().size(); i++) {
            boolean expected = Boolean.parseBoolean(result.expected().get(i));
            boolean closeBracket = StringUtil.isCloseBracket(result.inputs().get(i));
            assertEquals(expected, closeBracket);
        }
    }

    @Test
    void testExitsCloseBracket() throws IOException {
        String name = "test/exitsCloseBracket.txt";
        Result result = getResult(name);
        for (int i = 0; i < result.inputs().size(); i++) {
            String expression = result.inputs().get(i);
            boolean expected = Boolean.parseBoolean(result.expected().get(i));
            assertEquals(expected, StringUtil.exitsCloseBracket(expression));
        }
    }
}
