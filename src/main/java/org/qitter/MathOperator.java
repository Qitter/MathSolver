package org.qitter;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public enum MathOperator {

    ADD("+", BigDecimal::add,0),
    SUBTRACT("-",BigDecimal::subtract,0),
    MULTIPLY("*",BigDecimal::multiply,1),
    DIVIDE("/",(a,b)->a.divide(b,MathContext.DECIMAL32),1),
    POWER("^", (a,b)->a.pow(b.intValueExact()),2);

    public static final int
            ADD_LEVEL = ADD.level,
            MULTIPLY_LEVEL = MULTIPLY.level,
            POWER_LEVEL = POWER.level,
            FIRST_NUM_GROUP_INDEX = 1,
            OPERATOR_GROUP_INDEX = 2,
            SECOND_NUM_GROUP_INDEX = 3;
    private final String string;
    private final BiFunction<BigDecimal,BigDecimal,BigDecimal> operation;
    private final int level;
    private final Pattern pattern;
    @NotNull
    private static final HashMap<String, MathOperator> stringToOperator = new HashMap<>();
    @NotNull
    private static final HashMap<Integer,Set<MathOperator>> OPERATOR_LEVEL = new HashMap<>();
    @NotNull
    private static final HashMap<Integer,Pattern> OPERATOR_PATTERN = new HashMap<>();
    static {
        Arrays.stream(MathOperator.values()).forEach(s->{
            stringToOperator.put(s.string,s);
            put(s.level,s);
        });
        Arrays.stream(values()).forEach(s-> Optional.ofNullable(OPERATOR_PATTERN.get(s.level))
                .ifPresentOrElse((p)->{},()->{
                    StringJoiner sj = new StringJoiner("\\","(" + (s.level == ADD_LEVEL ? "-?" : "") + "\\d+)([\\","])(-?\\d+)");
                    getByLevel(s.level).forEach(o->sj.add(o.getString()));
                    OPERATOR_PATTERN.put(s.level,Pattern.compile(sj.toString()));
                }));
    }

    MathOperator(@NotNull String string, @NotNull BiFunction<BigDecimal, BigDecimal, BigDecimal> operation, int level) {
        this.string = string;
        this.operation = operation;
        this.level = level;
        pattern = Pattern.compile("(\\d+)[\\\\" + string + "](-?\\d+)");
    }
    @NotNull
    public String getString() {
        return string;
    }

    public int getLevel() {
        return level;
    }

    public static void put(int level,@NotNull MathOperator operator) {
        Optional.ofNullable(OPERATOR_LEVEL.get(level))
                .ifPresentOrElse(set->set.add(operator),(()->OPERATOR_LEVEL.put(level,new HashSet<>(Collections.singleton(operator)))));
    }

    @NotNull
    public static MathOperator getByName(@NotNull String string) {
        if (Optional.ofNullable(stringToOperator.get(string)).isPresent()) {
            return stringToOperator.get(string);
        }
        throw new IllegalArgumentException("Unknown operator: " + string);
    }

    @NotNull
    public BigDecimal apply(@NotNull BigDecimal a,@NotNull BigDecimal b) {
        return operation.apply(a,b);
    }
    @NotNull
    public BigDecimal apply(@NotNull String a,@NotNull String b) {
        return apply(new BigDecimal(a),new BigDecimal(b));
    }

    @NotNull
    public static String getAllString() {
        return Arrays.toString(MathOperator.values());
    }

    @NotNull
    public static Set<MathOperator> getByLevel(int level) {
        if (Optional.ofNullable(OPERATOR_LEVEL.get(level)).isPresent()) {
            return OPERATOR_LEVEL.get(level);
        }
        throw new IllegalArgumentException("Unknown operator level: " + level);
    }

    @NotNull
    public static Pattern getPatternByLevel(int level) {
        if (Optional.ofNullable(OPERATOR_PATTERN.get(level)).isPresent()) {
            return OPERATOR_PATTERN.get(level);
        }
        throw new IllegalArgumentException("Unknown operator level: " + level);
    }

    @NotNull
    public Pattern getPattern() {
        return pattern;
    }
}
