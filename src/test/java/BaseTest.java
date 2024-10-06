import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BaseTest {
    protected @NotNull Result getResult(String name) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(name)))
        );
        List<String> inputs = new ArrayList<>();
        List<String> expected = new ArrayList<>();
        boolean isInput = true;
        for (String string = reader.readLine(); string != null; string = reader.readLine()) {
            if (string.startsWith("//")) {
                isInput = !isInput;
                continue;
            }
            if (isInput) {
                inputs.add(string);
                isInput = false;
            }else {
                expected.add(string);
                isInput = true;
            }

        }
        return new Result(inputs, expected);
    }

    protected record Result(List<String> inputs, List<String> expected) {
    }
}
