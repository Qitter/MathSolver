package org.qitter.math;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public enum MathOperator {
    ADD("+", BigDecimal::add,0),
    SUBTRACT("-",BigDecimal::subtract,0),
    MULTIPLY("*",BigDecimal::multiply,1),
    DIVIDE("/",(a,b)->a.divide(b,MathContext.DECIMAL32),1),
    POWER("^", (a,b)->a.pow(b.intValueExact()),2),
    LOG("|", MathOperator::log,2),
    ROOT("&",MathOperator::root,2);

    public static final int
            ADD_LEVEL = ADD.level,
            MULTIPLY_LEVEL = MULTIPLY.level,
            POWER_LEVEL = POWER.level,
            FIRST_NUM_GROUP_INDEX = 1,
            OPERATOR_GROUP_INDEX = 2,
            SECOND_NUM_GROUP_INDEX = 3;
    @NotNull
    private final String operator;
    @NotNull
    private final BiFunction<BigDecimal,BigDecimal,BigDecimal> operation;
    private final int level;
    @NotNull
    private final Pattern pattern;
    @NotNull
    private static final HashMap<String, MathOperator> operatorOfEnum = new HashMap<>();
    @NotNull
    private static final HashMap<Integer,Set<MathOperator>> OPERATOR_LEVEL = new HashMap<>();
    @NotNull
    private static final HashMap<Integer,Pattern> OPERATOR_PATTERN = new HashMap<>();
    static {
        Map<Integer,StringJoiner> sjs = Map.of(
                ADD_LEVEL,new StringJoiner("\\","(-?\\d+)([\\","])(-?\\d+)"),
                MULTIPLY_LEVEL,new StringJoiner("\\","(\\d+)([\\","])(-?\\d+)"),
                POWER_LEVEL,new StringJoiner("\\","(\\d+)([\\","])(-?\\d+)")
        );
        Arrays.stream(MathOperator.values()).forEach(s->{
            operatorOfEnum.put(s.operator,s);
            put(s.level,s);
            sjs.get(s.level).add(s.operator);
        });
        sjs.forEach((l,s)-> OPERATOR_PATTERN.put(l,Pattern.compile(s.toString())));
    }

    MathOperator(@NotNull String operator, @NotNull BiFunction<BigDecimal, BigDecimal, BigDecimal> operation, int level) {
        this.operator = operator;
        this.operation = operation;
        this.level = level;
        pattern = Pattern.compile("(\\d+)[\\\\" + operator + "](-?\\d+)");
    }
    @NotNull
    public String getOperator() {
        return operator;
    }

    private static void put(int level,@NotNull MathOperator operator) {
        Optional.ofNullable(OPERATOR_LEVEL.get(level))
                .ifPresentOrElse(set->set.add(operator),(()->OPERATOR_LEVEL.put(level,new HashSet<>(Collections.singleton(operator)))));
    }

    /**
     * 用过操作符获取枚举对象
     * @param operator 操作符
     * @return 枚举对象
     */
    @NotNull
    public static MathOperator getByOperator(@NotNull String operator) {
        if (Optional.ofNullable(operatorOfEnum.get(operator)).isPresent()) {
            return operatorOfEnum.get(operator);
        }
        throw Logger.getLogger().errorAndClose(new IllegalArgumentException("朕不知此符为何物 : " + operator));
    }

    /**
     * 执行运算符操作
     * @param a 数1
     * @param b 数2
     * @return 运算结果
     */
    @NotNull
    public BigDecimal apply(@NotNull BigDecimal a,@NotNull BigDecimal b) {
        return operation.apply(a,b);
    }

    /**
     * 执行运算符操作
     * @param a 数1
     * @param b 数2
     * @return 运算结果
     */
    @NotNull
    public BigDecimal apply(@NotNull String a,@NotNull String b) {
        return apply(new BigDecimal(a),new BigDecimal(b));
    }

    @NotNull
    public static String getAllOperator() {
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

    /**
     * 求log(base,b)
     * @param base 被开根数
     * @param b 开根次数
     * @return 开根结果
     */
    @NotNull
    public static BigDecimal log(@NotNull BigDecimal base, @NotNull BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            throw Logger.getLogger().errorAndClose(new IllegalArgumentException("log(0) is undefined"));
        }
        Logger.getLogger().log("MathOperator.log 以" + base + "为底" + b);
        return DIVIDE.apply(BigDecimal.valueOf(Math.log10(b.doubleValue())),BigDecimal.valueOf(Math.log10(base.doubleValue())));
    }

    /**
     * 求根号a的b次方
     * @param a 被开根数
     * @param b 开根次数
     * @return 开根结果
     */
    public static BigDecimal root(@NotNull BigDecimal a,@NotNull BigDecimal b) {
        if(a.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return BigDecimalUtil.setScale(BigDecimal.valueOf(
                Math.pow(10,DIVIDE.apply(log(BigDecimal.TEN,a),b).doubleValue())
        ),0,RoundingMode.HALF_DOWN);
    }

    @NotNull
    public Pattern getPattern() {
        return pattern;
    }
}
