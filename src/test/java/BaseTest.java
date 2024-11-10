import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.qitter.config.ConfigReader;
import org.qitter.log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class BaseTest {
    @NotNull
    protected Result getResult(String name) {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream(name)))
        );
        List<String> inputs = new ArrayList<>();
        List<String> expected = new ArrayList<>();
        boolean isInput = true;
        try {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Result(inputs, expected);
    }

    protected record Result(@NotNull List<String> inputs,@NotNull List<String> expected) {
    }

    protected void test(@NotNull Function<String,String> consumer, @NotNull Result result) {
        for (int i = 0; i < result.inputs().size(); i++) {
            String lastResult = consumer.apply(result.inputs().get(i));
            try{
                Assertions.assertEquals(result.expected().get(i), lastResult);
            }catch(AssertionFailedError e){
                System.out.println("在索引为 : [" + i +"] 处发生错误\n输入 : " + result.inputs().get(i) + "\n期望 : " + result.expected().get(i) + "\n结果 : " + lastResult + "\n");
            }

        }
    }

    @AfterAll
    static void close() {
        Logger.getLogger().close();
        ConfigReader.closeAllConfigReaders();
    }
}
