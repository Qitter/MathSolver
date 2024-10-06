package org.qitter.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class StringUtil {
    /**
     * 提取最外层括号的内容
     * 例如: (a + b) + (c + d) -> [a + b, c + d]
     * 例如：(a*(a+b)) + (b*(a+b)) -> [a*(a+b), b*(a+b)]
     * @param expression 表达式
     * @return 括号中的内容
     */
    @NotNull
    public static ArrayList<String> extractBracket(@NotNull String expression) {
        ArrayList<String> tokens = new ArrayList<>();
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
                    tokens.add(expression.substring(startIndex + 1, i));
                }
            }
        }
        if(!bracketStack.isEmpty()) {
            throw new RuntimeException("\"" + expression + "\"括号未闭合");
        }
        return tokens;
    }

    public static boolean isCloseBracket(@NotNull String expression) {
        ArrayDeque<Character> characters = new ArrayDeque<>();
        //用于检查括号后是否还有字符，若它的值等于总长-1，就表示没有字符
        int lastIndex = -2;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            switch (c) {
                case '(': {
                    characters.push(c);
                    break;
                }
                case ')': {
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

}
