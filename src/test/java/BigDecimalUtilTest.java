import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.qitter.util.BigDecimalUtil;

import java.math.BigDecimal;

public class BigDecimalUtilTest extends BaseTest {
    @Test
    void stripTrailingZerosTest() {
        test((s -> BigDecimalUtil.stripTrailingZeros(new BigDecimal(s)).toString()), getResult("test/stripTrailingZerosTest.txt"));
    }
}
