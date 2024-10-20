package org.qitter.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class StringUtil {
    public record BracketResult(@NotNull String context, int startIndex, int endIndex) {
    }

    /**
     * 提取最外层括号的内容
     * 例如: (a + b) + (c + d) -> [a + b, c + d]
     * 例如：(a*(a+b)) + (b*(a+b)) -> [a*(a+b), b*(a+b)]
     * @param expression 表达式
     * @return 括号中的内容
     */
    @NotNull
    public static ArrayList<BracketResult> extractBracket(@NotNull String expression) {
        ArrayList<BracketResult> tokens = new ArrayList<>();
        int startIndex = -1;
        ArrayDeque<Integer> bracketStack = new ArrayDeque<>();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if(c == '(') {
                if(bracketStack.isEmpty()) {
                    startIndex = i;
                }
                bracketStack.push(i);
            }else if(c == ')') {
                if(bracketStack.isEmpty()) {
                    throw new RuntimeException("索引为 : " + i + "\"" + expression + "\"的右括号未闭合");
                }
                bracketStack.pop();
                if(bracketStack.isEmpty()) {
                    String substring = expression.substring(startIndex + 1, i);
                    tokens.add(new BracketResult(substring, startIndex, i));
                }
            }
        }
        if(!bracketStack.isEmpty()) {
            throw new RuntimeException("\"" + expression + "\"括号未闭合");
        }
        return tokens;
    }

    /**
     * 检查是否为闭合括号
     * 如果输入为 ()( ，仍会返回false
     * @param expression 表达式
     * @return true表示是闭合括号
     */
    public static boolean isCloseBracket(@NotNull String expression) {
        if(expression.charAt(0) != '(' || expression.charAt(expression.length() - 1) != ')') {
            return false;
        }
        ArrayDeque<Character> characters = new ArrayDeque<>();
        //用于检查括号后是否还有字符，若它的值等于 总长-1，就表示没有字符
        int lastIndex = -2;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            switch (c) {
                case '('-> characters.push(c);
                case ')'-> {
                    if(characters.isEmpty()) {
                        return false;
                    }
                    characters.pop();
                    lastIndex = i;
                }
            }
        }
        return characters.isEmpty() && lastIndex == expression.length() - 1;
    }

    /**
     * 判断表达式是否所有括号都为闭合括号
     * @param expression 表达式
     * @return true表示所有括号都闭合
     */
    public static boolean exitsCloseBracketAll(@NotNull String expression) {
        ArrayDeque<Integer> bracketStack = new ArrayDeque<>();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if(c == '(') {
                bracketStack.push(i);
            }else if(c == ')') {
                if(bracketStack.isEmpty()) {
                    return false;
                }
                bracketStack.pop();
            }
        }
        return bracketStack.isEmpty();
    }

    /**
     * 判断表达式是否包含闭合括号
     * @param expression 表达式
     * @return true表示包含
     */
    public static boolean exitsCloseBracket(@NotNull String expression) {
        for(int i = 0; i < expression.length() -1; i++) {
            for (int j = i; j < expression.length(); j++) {
                String substring = expression.substring(i, j + 1);
                if (isCloseBracket(substring)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasBracket(@NotNull String expression) {
        return expression.contains("(") || expression.contains(")");
    }

    /**
     * 将表达式中的符号格式化
     * -3++++++4 -> -3+4
     * -3----4 -> -3+4
     * -3++-4 -> -3-4
     * @param expression 字符串
     * @return 去除负号后的字符串
     */
    @NotNull
    public static String formatNumber(@NotNull String expression) {
        StringBuilder buff = new StringBuilder();
        // isNegative表示是否为负数，-1表示负数，1表示正数 0表示未判断
        int isNegative = 0;
        // 用来存储符号,0为-,1为\0,2为+
        String[] map = {"-","","+"};
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            switch (c) {
                case '+' ->{}
                case '-' ->
                    // 0 -> -1, -1 -> 1, 1 -> -1
                    isNegative = isNegative == 0 ? -1 : -isNegative;
                default ->{
                    //因为isNegative的值为0,-1,1，所以当isNegative为0时，表示为正数，直接添加符号
                    buff.append(map[isNegative + 1]).append(c);
                    isNegative = 0;
                }
            }
        }
        return buff.toString();
    }
}
