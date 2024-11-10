package org.qitter.math;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.util.BigDecimalUtil;

import java.math.BigDecimal;
import java.util.Map;

public class MathResult {
    public enum ResultType {
        INTEGER,EXPRESSION,ERROR
    }
    @NotNull
    private final ResultType type;
    @NotNull
    private final BigDecimal value;
    @NotNull
    private final MathExpression expression;
    private MathResult(@NotNull ResultType type,@NotNull BigDecimal value,@NotNull String expression) {
        this.value = value;
        this.expression = new MathExpression(expression);
        this.type = type;
        Logger.getLogger().log(this,"已创建,参数是 : type : " + type + ",value :" + value + ",expression : " + expression);
    }

    @Contract("_ -> new")
    public static @NotNull MathResult ofNumber(@NotNull String value) {
        Logger.getLogger().log("ofNumber",value);
        return new MathResult(ResultType.INTEGER, BigDecimalUtil.stripTrailingZeros(new BigDecimal(value)),value);
    }
    @Contract("_ -> new")
    public static @NotNull MathResult ofNumber(@NotNull BigDecimal value) {
        Logger.getLogger().log("ofNumber",value.toString());
        return new MathResult(ResultType.INTEGER,value,value.toString());
    }
    @Contract("_ -> new")
    public static @NotNull MathResult ofExpression(@NotNull String expression) {
        Logger.getLogger().log("ofExpression方法",expression);
        return new MathResult(ResultType.EXPRESSION,BigDecimal.ZERO,expression);
    }
    @Contract("_ -> new")
    public static @NotNull MathResult ofError(@NotNull String expression) {
        Logger.getLogger().log("ofError方法",expression);
        return new MathResult(ResultType.ERROR,BigDecimal.ZERO,expression);
    }
    @NotNull
    public ResultType getType() {
        return type;
    }

    public double toDouble() {
        return value.doubleValue();
    }

    public long toLong() {
        return value.longValue();
    }

    public @NotNull BigDecimal getValue() {
        return value;
    }

    public int toInt() {
        return value.intValue();
    }

    @NotNull
    public MathExpression getExpression() {
        return expression;
    }

    @NotNull
    public MathExpression substitute(@NotNull Map<Character,BigDecimal> variables) {
        if(type != ResultType.EXPRESSION) {
            throw Logger.getLogger().errorAndClose(new IllegalStateException("尝试在一个没有未知数的表达式中代入, 表达式 :" + expression));
        }
        return expression.substituteAndWorkOut(variables);
    }

    @Override
    public String toString() {
        return switch (type) {
            case INTEGER -> value.toString();
            case EXPRESSION -> expression.toString();
            case ERROR -> "Error: " + expression;
        };
    }
}
