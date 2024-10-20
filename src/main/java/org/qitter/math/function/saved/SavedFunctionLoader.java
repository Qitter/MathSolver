package org.qitter.math.function.saved;

import org.jetbrains.annotations.NotNull;
import org.qitter.config.ConfigReader;
import org.qitter.math.MathExpression;

import java.util.Map;

public class SavedFunctionLoader {
    @NotNull
    private static final String FUNCTIONS_FILE = "functions.txt";
    @NotNull
    private static final SavedFunctionLoader instance = new SavedFunctionLoader();
    @NotNull
    private final ConfigReader reader;
    private SavedFunctionLoader() {
        reader = ConfigReader.create(FUNCTIONS_FILE, Map.of());
    }

    public void load() {
        reader.forEach((name,exp)-> put(name, new MathExpression(exp)));
    }

    public void put(@NotNull String name, @NotNull MathExpression expression) {
        SavedFunction.addFunction(name,expression);
    }

    @NotNull
    public static SavedFunctionLoader getInstance() {
        return instance;
    }
}
