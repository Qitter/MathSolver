package org.qitter.math.function.saved;

import org.jetbrains.annotations.NotNull;
import org.qitter.math.MathExpression;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SavedFunction {
    @NotNull
    private static final Map<String,SavedFunction> functions = new HashMap<>();
    @NotNull
    private final String name;
    @NotNull
    private final MathExpression expression;

    private SavedFunction(@NotNull String name,@NotNull MathExpression expression) {
        this.name = name;
        this.expression = expression;
    }

    public static void addFunction(@NotNull String name,@NotNull MathExpression function) {
        functions.put(name,new SavedFunction(name,function));
    }

    @NotNull
    public static Optional<SavedFunction> getFunction(@NotNull String name) {
        return Optional.ofNullable(functions.get(name));
    }

    @NotNull
    public MathExpression getMathExpression() {
        return expression;
    }

    @NotNull
    public String getName() {
        return name;
    }

}
