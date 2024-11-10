package org.qitter.util.saved;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.math.MathExpression;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SavedFunction {
    @NotNull
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("([a-zA-z]+)\\((.*)\\)");
    private static final int FUNCTION_NAME_GROUP = 1;
    private static final int FUNCTION_ARGS_GROUP = 2;
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
    public static String parseFunctions(@NotNull String input) {
        Matcher m = FUNCTION_PATTERN.matcher(input);
        StringBuilder result = new StringBuilder(input);
        m.results().forEach(r->{
            String name = r.group(FUNCTION_NAME_GROUP);
            String group = r.group(FUNCTION_ARGS_GROUP);
            List<MathExpression> args = Arrays.stream(group.split(",")).map(MathExpression::new).toList();

            getFunction(name).ifPresentOrElse(f-> result.replace(r.start(),r.end(),f.substitute(args).getExpression()),()->{
                throw
                        Logger.getLogger().errorAndClose(new IllegalArgumentException("Function " + name + " not found"));
            });
        });
        return result.toString();
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

    @NotNull
    public MathExpression substitute(@NotNull Map<Character,MathExpression> variables) {
        return expression.substituteExpression(variables);
    }

    @NotNull
    public MathExpression substitute(@NotNull Collection<MathExpression> expressions) {
        Iterator<Character> varIte = this.expression.getVariables().iterator();
        Iterator<MathExpression> expIte = expressions.iterator();
        Map<Character,MathExpression> result = new HashMap<>();
        while (varIte.hasNext() && expIte.hasNext()) {
            result.put(varIte.next(),expIte.next());
        }
        return substitute(result);
    }
}
