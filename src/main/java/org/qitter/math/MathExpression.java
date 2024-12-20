package org.qitter.math;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.util.BigDecimalUtil;
import org.qitter.util.StringUtil;
import org.qitter.util.saved.SavedFunction;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathExpression {
    @NotNull
    private static final Pattern TOKEN_PATTERN = Pattern.compile("[^+]+");
    /* 变量名只能是一个字符 */
    @NotNull
    public static final Pattern VARIABLE_PATTERN = Pattern.compile("[a-zA-Z]");

    /* 变量名只能是一个字符 */
    @NotNull
    private final Set<Character> variables;
    @NotNull
    private List<String> tokens;
    private String expression;
    public MathExpression(@NotNull String expression) {
        expression = parseFunctions(expression);
        this.tokens = parseTokens(expression);
        this.expression = expression;
        this.variables = new HashSet<>();
        VARIABLE_PATTERN.matcher(expression).results().forEach(result -> variables.add(result.group().charAt(0)));
        Logger.getLogger().log(this,"expression : " + expression,"tokens : " + tokens);
    }

    private @NotNull String parseFunctions(@NotNull String expression) {
        return SavedFunction.parseFunctions(expression);
    }

    /**
     * 解析表达式中的所有项
     * 例如：2+3*4-5/2
     * 解析为：[2, 3*4, 2.5]
     * 例如：2+3*(4-5)/2
     * 解析为：[2, 3, 4, -5, 2]
     * @param expression 表达式
     * @return 所有项
     */
    public static @NotNull ArrayList<String> parseTokens(@NotNull String expression) {
        if(expression.equals("(0-0)^2-4*(0-4)*(1-0))&2")) {
            System.out.println("expression = " + expression);
        }
        Logger.getLogger().log("进入 parseToken方法",expression);
        ArrayList<String> tokens = new ArrayList<>();
        Matcher m = TOKEN_PATTERN.matcher(expression);
        if (!StringUtil.exitsCloseBracket(expression)) {
            m.results().forEach(result -> tokens.add(result.group()));
            return tokens;
        }
        StringBuilder temp = new StringBuilder();
        while (m.find()) {
            temp.append(m.group());
            if(StringUtil.hasBracket(temp.toString()) && !StringUtil.exitsCloseBracketAll(temp.toString())) {
                temp.append("+");
                continue;
            }
            tokens.add(temp.toString());
            temp.delete(0, temp.length());
        }
        Logger.getLogger().log("完成 parseToken方法",expression,tokens.toString());
        return tokens;
    }

    /**
     * 计算每一项的值
     */
    private void workOutAllTokens() {
        List<String> list = tokens.stream().map(MathExpression::workOutToken).toList();
        Logger.getLogger().log(this,"workOutAllTokens方法","tokens:" + tokens,"list:" + list);
        tokens = list;
    }

    /**
     * 计算一个无变量的表达式
     * @param expression 表达式
     * @return 计算结果
     */
    @NotNull
    private static BigDecimal workOut(@NotNull MathExpression expression) {
        if(VARIABLE_PATTERN.matcher(expression.getExpression()).matches()) {
            throw Logger.getLogger().errorAndClose(new IllegalArgumentException("无法计算含有未知数的表达式,若要代入请调用 substitute方法 \t \"原式为"+ expression + "\""));
        }
        expression.workOutAllTokens();
        BigDecimal decimal = BigDecimal.ZERO;
        for (String token : expression.tokens) {
            decimal = decimal.add(new BigDecimal(token));
        }

        Logger.getLogger().log("原式等于 : " + expression,"结果为 : " + decimal);
        expression.expression = decimal.toString();
        return BigDecimalUtil.stripTrailingZeros(decimal);
    }

    @NotNull
    public BigDecimal workOut() {
        return workOut(this);
    }

    /**
     * 仅仅只能计算一个不带变量的无加法运算,无括号的式子
     * @param token 表达式
     * @return 计算结果
     * @see MathExpression#workOutLevelToken(String, int)
     */
    @NotNull
    public static String workOutToken(@NotNull String token) {
        final StringBuilder stringBuilder = new StringBuilder(token);
        AtomicInteger count = new AtomicInteger();
        StringUtil.extractBracket(stringBuilder.toString()).forEach(br->{
            String woken = workOutToken(br.context());
            stringBuilder.replace(br.startIndex() - count.get(), br.endIndex()+1 - count.get(), woken);
            count.addAndGet(br.endIndex() - br.startIndex() - woken.length() +1);
        });
        Logger.getLogger().log("完成 递归workOutToken方法",token);
        token = stringBuilder.toString();
        var buffer = workOutLevelToken(token, MathOperator.POWER_LEVEL);
        buffer = workOutLevelToken(buffer, MathOperator.MULTIPLY_LEVEL);
        buffer = workOutLevelToken(buffer, MathOperator.ADD_LEVEL);
        buffer = StringUtil.formatNumber(buffer);
        Logger.getLogger().log("workOutToken方法",token,buffer);
        return buffer;
    }



    /**
     * 计算不同运算等级的项
     * @param token 表达式
     * @param level 运算等级
     * @return 计算结果
     * @see MathOperator#POWER_LEVEL
     * @see MathOperator#MULTIPLY_LEVEL
     * @see MathOperator#ADD_LEVEL
     */
    @NotNull
    private static String workOutLevelToken(@NotNull String token, int level) {
        Logger.getLogger().log("进入 workOutLevelToken方法",token,"运算等级为 : " + level);
        StringBuilder buffer = new StringBuilder(token);
        Matcher powMatcher = MathOperator.getPatternByLevel(level).matcher(buffer);
        while (powMatcher.find()) {
            var left = powMatcher.start(MathOperator.FIRST_NUM_GROUP_INDEX);
            var right = powMatcher.end(MathOperator.SECOND_NUM_GROUP_INDEX);
            BigDecimal result = MathOperator.getByOperator(powMatcher.group(MathOperator.OPERATOR_GROUP_INDEX))
                    .apply(powMatcher.group(MathOperator.FIRST_NUM_GROUP_INDEX), powMatcher.group(MathOperator.SECOND_NUM_GROUP_INDEX));

            buffer.replace(left, right, result.toString());
            powMatcher = MathOperator.getPatternByLevel(level).matcher(buffer);
        }
        Logger.getLogger().log("完成 workOutLevelToken方法",token,buffer);
        return buffer.toString();
    }

    /**
     * 代入求值，必须是所有变量对应的值才能代入求值
     * @param variables 所有的变量和对应的值 变量名只能是一个字符
     * @return 计算结果
     */
    @NotNull
    public MathExpression substituteAndWorkOut(@NotNull Map<Character,BigDecimal> variables) {
        MathExpression result = substitute(variables);
        String beforeExpression = result.expression;
        try{
            result.workOut();
        }catch(NumberFormatException e){
            Logger.getLogger().log(this,"可能仍含有未知量","原式 :" + beforeExpression,"结果 : "+ result.expression);
            throw Logger.getLogger().errorAndClose(e);
        }

        Logger.getLogger().log(this,"原式 :" + beforeExpression,"结果 : "+ result);
        return result;
    }

    /**
     * 代入表达式
     * @param variables 变量和对应的值 变量名只能是一个字符
     * @return 代入后 {@link MathExpression}
     */
    @NotNull
    public MathExpression substitute(@NotNull Map<Character,BigDecimal> variables) {
        StringBuilder expressionBuilder = new StringBuilder();
        this.expression.chars().forEach(
                c-> Optional.ofNullable(variables.get((char)c))
                        .ifPresentOrElse(expressionBuilder::append,()->expressionBuilder.append((char) c))
        );
        Logger.getLogger().log(this,"代入前",expression,"代入后",expressionBuilder);
        return new MathExpression(expressionBuilder.toString());
    }

    /**
     * 代入的表达式如果没有变量，便会调用{@link #workOut()}方法算出值再代入
     * 代入的表达式如果变量超过一个，则会在表达式外加上括号
     * @param variables 所有的变量和对应的表达式 变量名只能是一个字符
     * @return 代入后的 {@link MathExpression}
     */
    public MathExpression substituteExpression(@NotNull Map<Character,MathExpression> variables) {
        StringBuilder expressionBuilder = new StringBuilder();
        this.expression.chars().forEach(c-> Optional.ofNullable(variables.get((char)c))
                        .ifPresentOrElse(
                                m-> expressionBuilder.append(m.isSingleToken()?m.expression:"("+m.expression+")"), ()->expressionBuilder.append((char) c)
                        )
        );
        Logger.getLogger().log(this,"代入前",expression,"代入后",expressionBuilder);
        return new MathExpression(expressionBuilder.toString());
    }

    /**
     * 获得所有的变量
     * 变量顺序不分先后
     * @return 所有变量
     */
    public @NotNull Set<Character> getVariables() {
        return variables;
    }

    /**
     * 获得变量个数
     * @return 变量个数
     */
    public int getVariableCount() {
        return variables.size();
    }

    @NotNull
    public String getExpression() {
        return expression;
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString()+"["+expression+"]";
    }

    public boolean hasVariable() {
        return !variables.isEmpty();
    }

    @NotNull
    public MathResult toMathResult() {
        if(hasVariable()) {
            return MathResult.ofExpression(expression);
        }
        return MathResult.ofNumber(expression);
    }

    /**
     * 判断是否只有一个项
     * @return true 表示只有一个项
     */
    public boolean isSingleToken() {
        return tokens.size() == 1;
    }

    /**
     * 通过静态方法创建{@link MathExpression}
     * @param values 所有的值
     */
    @NotNull
    public static MathExpression of(@NotNull BigDecimal... values) {
        BigDecimal result = BigDecimal.ZERO;
        for (@NotNull BigDecimal value : values) {
            result = result.add(value);
        }
        return new MathExpression(result.toString());
    }
}
