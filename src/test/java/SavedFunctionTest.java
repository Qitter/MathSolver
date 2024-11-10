import org.junit.jupiter.api.Test;
import org.qitter.math.MathExpression;
import org.qitter.util.saved.SavedFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SavedFunctionTest extends BaseTest {
    @Test
    void test() {
        SavedFunction.addFunction("add", new MathExpression("a+b"));
        assertEquals("1+1",
                SavedFunction.parseFunctions("add(1,1)")
        );
        SavedFunction.addFunction("add", new MathExpression("a+b"));
        assertEquals("1+1",
                SavedFunction.parseFunctions("add(1,1)")
        );


        SavedFunction.addFunction("sub", new MathExpression("a-b"));
        assertEquals("1-1",
                SavedFunction.parseFunctions("sub(1,1)")
        );

        SavedFunction.addFunction("root", new MathExpression("a&2"));
        assertEquals("1&2",
                SavedFunction.parseFunctions("root(1)")
        );
    }
}
