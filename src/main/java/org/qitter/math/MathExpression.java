package org.qitter.math;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.util.StringUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathExpression {
    @NotNull
    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\(.*?\\)");
    @NotNull
    private static final Pattern TOKEN_PATTERN = Pattern.compile("[^+]+");
    @NotNull
    public static final Pattern VARIABLE_PATTERN = Pattern.compile("[a-zA-Z]+");

    @NotNull
    private final ArrayList<String> tokens;
    private String expression;
    public MathExpression(@NotNull String expression) {
        this.tokens = parseTokens(expression);
        this.expression = expression;
        Logger.getLogger().log(this,"expression : " + expression,"tokens : " + tokens);
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

    public void simplify() {
        List<String> list = tokens.stream().map(MathExpression::workOutToken).toList();
        Logger.getLogger().log(this,"simplify方法","tokens:" + tokens,"list:" + list);
        tokens.clear();
        tokens.addAll(list);
    }

    @NotNull
    private static String workOut(@NotNull MathExpression expression) {
        if(VARIABLE_PATTERN.matcher(expression.getExpression()).matches()) {
            throw Logger.getLogger().error(new IllegalArgumentException("无法计算含有未知数的表达式,若要代入请调用 substitute方法 \t \"原式为"+ expression + "\""));
        }
        expression.simplify();
        BigDecimal decimal = new BigDecimal(0);
        for (String token : expression.tokens) {
            decimal = decimal.add(new BigDecimal(token));
        }

        Logger.getLogger().log("原式等于 : " + expression,"结果为 : " + decimal);
        return expression.expression = decimal.toString();
    }

    @NotNull
    public String workOut() {
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
        Matcher bracketM = BRACKET_PATTERN.matcher(stringBuilder);
        Logger.getLogger().log("进入 递归workOutToken方法",token);
        while (bracketM.find()) {
            String group = bracketM.group();
            stringBuilder.replace(bracketM.start(), bracketM.end(), workOutToken(group.substring(1, group.length() - 1)));
            bracketM = BRACKET_PATTERN.matcher(stringBuilder);
        }
        Logger.getLogger().log("完成 递归workOutToken方法",token);
        token = stringBuilder.toString();
        var buffer = workOutLevelToken(token, MathOperator.POWER_LEVEL);
        buffer = workOutLevelToken(buffer, MathOperator.MULTIPLY_LEVEL);
        buffer = workOutLevelToken(buffer, MathOperator.ADD_LEVEL);
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
            BigDecimal result = MathOperator.getByName(powMatcher.group(MathOperator.OPERATOR_GROUP_INDEX))
                    .apply(powMatcher.group(MathOperator.FIRST_NUM_GROUP_INDEX), powMatcher.group(MathOperator.SECOND_NUM_GROUP_INDEX));

            buffer.replace(left, right, result.toString());
            powMatcher = MathOperator.getPatternByLevel(level).matcher(buffer);
        }
        Logger.getLogger().log("完成 workOutLevelToken方法",token,buffer);
        return buffer.toString();
    }

    @NotNull
    public MathExpression substitute(@NotNull Map<String,Double> variables) {
        // TODO
        return this;
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
}
