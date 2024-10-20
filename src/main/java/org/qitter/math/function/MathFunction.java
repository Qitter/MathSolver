package org.qitter.math.function;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.math.MathExpression;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数学函数函数
 */
public abstract class MathFunction {
    @NotNull
    private static final Pattern FUNCTION_PATTERN = Pattern.compile("([a-zA-z])?=(.*)");
    private static final int FUNCTION_NAME_GROUP_INDEX =1,
            FUNCTION_EXPRESSION_GROUP_INDEX = 2;
    @NotNull
    private final MathExpression expression;
    @SuppressWarnings("unused")
    public  static final char
            COEFFICIENT_A = 'a',
            COEFFICIENT_B = 'b',
            COEFFICIENT_C = 'c',
            COEFFICIENT_D = 'd',
            COEFFICIENT_E = 'e',
            COEFFICIENT_F = 'f',
            COEFFICIENT_G = 'g',
            COEFFICIENT_H = 'h',
            COEFFICIENT_I = 'i',
            COEFFICIENT_J = 'j',
            COEFFICIENT_K = 'k',
            COEFFICIENT_L = 'l',
            COEFFICIENT_M = 'm',
            COEFFICIENT_N = 'n',
            COEFFICIENT_O = 'o',
            COEFFICIENT_P = 'p',
            COEFFICIENT_Q = 'q',
            COEFFICIENT_R = 'r',
            COEFFICIENT_S = 's',
            COEFFICIENT_T = 't',
            COEFFICIENT_U = 'u',
            COEFFICIENT_V = 'v',
            COEFFICIENT_W = 'w',
            FUNCTION_VARIABLE = 'x',
            FUNCTION_RESULT = 'y';
    private final char variableName,functionName;
    @NotNull
    private final Map<Character, BigDecimal> coefficients;

    /**
     * 对于子类的构造函数
     * @param expression 表达式 可带y也可不带，若不带函数表达式的名字默认为y
     * @param simpleForm 函数的简单形式，例如y=ax+b
     * @param solveCoefficientsMethod 求解系数的方法,参数为表达式和自变量的名字，用来从
     */
    protected MathFunction(@NotNull String expression,
                           @NotNull MathExpression simpleForm,
                           @NotNull BiFunction<MathExpression,Character,Map<Character,BigDecimal>> solveCoefficientsMethod) {
        Matcher matcher = FUNCTION_PATTERN.matcher(expression);
        if (!matcher.find()) {
            throw Logger.getLogger().errorAndClose(
                    new IllegalArgumentException(expression + "，未找到变量 不符合函数的格式，格式为 : y=" + simpleForm.getExpression())
            );
        }
        try{
            this.expression = new MathExpression(matcher.group(FUNCTION_EXPRESSION_GROUP_INDEX));
            if(this.expression.getVariableCount() > 1) {
                throw Logger.getLogger().errorAndClose(
                        new IllegalArgumentException(expression + "，自变量不唯一 不符合函数的格式，格式为 : y=" + simpleForm.getExpression())
                );
            }
        }catch(RuntimeException e){
            throw Logger.getLogger().errorAndClose(
                    new IllegalArgumentException(expression + "，不符合函数的格式，格式为 : y=" + simpleForm.getExpression(),e)
            );
        }
        Iterator<Character> iterator = this.expression.getVariables().iterator();
        this.variableName = iterator.hasNext()?iterator.next():'\0';
        this.functionName = Optional.ofNullable(matcher.group(FUNCTION_NAME_GROUP_INDEX)).orElse("y").charAt(0);
        this.coefficients = solveCoefficientsMethod.apply(this.expression,variableName);
    }

    @NotNull
    public MathExpression getExpression() {
        return expression;
    }

    @NotNull
    public Map<Character, BigDecimal> getCoefficients() {
        return coefficients;
    }

    @NotNull
    public BigDecimal getCoefficient(char name){
        return Optional.ofNullable(coefficients.get(name)).orElseThrow(()-> Logger.getLogger().errorAndClose(
                new IllegalArgumentException("[" + expression.getExpression() + "]未找到系数 : " + name)));
    }

    public char getVariableName(){
        return variableName;
    }

    public char getFunctionName(){
        return functionName;
    }

    @NotNull
    public abstract BigDecimal getY(@NotNull BigDecimal x);

    @NotNull
    public abstract BigDecimal getX(@NotNull BigDecimal y);

    @NotNull
    public abstract MathExpression asSimpleForm();
}
