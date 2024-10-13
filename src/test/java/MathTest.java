import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.MathContext;

public class MathTest {
    @Test
    void test() {
        int n = 100;
        long start = System.currentTimeMillis();
        int v1 = useLoop(n);
        long end = System.currentTimeMillis();
        System.out.println("useLoop: " + (end - start) + "ms");
        System.out.println(v1);
        start = System.currentTimeMillis();
        int v2 = useTest(n);
        end = System.currentTimeMillis();
        System.out.println("useTest: " + (end - start) + "ms");
        System.out.println(v2);
        Assertions.assertEquals(v1,v2);
    }

    int useTest(int n) {
        BigDecimal b = new BigDecimal(n);
        BigDecimal multiply = b.add(BigDecimal.ONE).multiply(b);
        BigDecimal divide = multiply.divide(new BigDecimal(2), MathContext.DECIMAL32);
        BigDecimal add = divide
                .add(BigDecimal.valueOf(n / 2))
                .divide(BigDecimal.valueOf(2), MathContext.DECIMAL128);
        return add
                .add(BigDecimal.valueOf(n % 2 == 0 ? n : 0)).intValue();
    }

    int useLoop(int n) {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                sum += i;
            }
        }
        return sum;
    }
}
