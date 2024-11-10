package org.qitter.util;

import org.jetbrains.annotations.NotNull;
import org.qitter.log.Logger;
import org.qitter.math.MathExpression;
import org.qitter.math.function.MathFunction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.qitter.util.BigDecimalUtil.isArithmeticProgression;

public class MathExpressionUtil {
    /**
     * 获取函数的次数
     * 原理：
     * 将x从0开始，每次加1，代入表达式，依次相减，若结果为等差数列，相减的次数即为函数的次数
     * 例如,
     *  <html>
     *      <h1>y=x</h1>
     *      <p1>
     *          y1=0,y2=1,y3=2,y4=3，为等差数列，次数为1
     *      </p1>
     *      <h1>y=x^2</h1>
     *      <p1>
     *          y1=0,y2=1,y3=4,y4=9
     *      </p1>
     *      <br>
     *      <p1>y2-y1=1,y3-y2=3,y4-y3=5，为等差数列，次数为2</p1>
     *      <h1>y=x^3</h1>
     *      <p1>
     *          y1=0,y2=1,y3=8,y4=27,y5=64
     *          <br>
     *      </p1>
     *      <p1>
     *          t1 = y2-y1=1,t2 = y3-y2=7,t3 = y4-y3=19,t4 = y5-y4=37
     *          <br>
     *      </p1>
     *      <p1>
     *          t2-t1=6,t3-t2=12,t4-t3=18，为等差数列，次数为3
     *          <br>
     *      </p1>
     *  <html/>
     * @throws IllegalArgumentException 如果函数不是函数，则抛出异常
     * @param expression 函数表达式
     * @return 函数的次数
     */
    public static int getFunctionTimes(@NotNull MathExpression expression) {
        if(!isFunction(expression)) {
            return 0;
        }
        AtomicInteger count = new AtomicInteger(0);
        var v = expression.getVariables().iterator().next();

        BigDecimal x = BigDecimal.ZERO;
        var y1 = expression.substituteAndWorkOut(Map.of(v, x)).toMathResult().getValue();
        var y2 = expression.substituteAndWorkOut(Map.of(v, x = x.add(BigDecimal.ONE))).toMathResult().getValue();
        var y3 = expression.substituteAndWorkOut(Map.of(v, x = x.add(BigDecimal.ONE))).toMathResult().getValue();
        count.addAndGet(1);
        if (isArithmeticProgression(y1,y2,y3)) {
            Logger.getLogger().log(expression,"是一个" + count + "次函数");
            return count.get();
        }

        count.addAndGet(1);
        var y4 = expression.substituteAndWorkOut(Map.of(v, x = x.add(BigDecimal.ONE))).toMathResult().getValue();
        List<BigDecimal> topListOfY = new ArrayList<>(List.of(
                y2.subtract(y1),y3.subtract(y2), y4.subtract(y3)
        ));
        List<BigDecimal> listOfLastY = new LinkedList<>();
        listOfLastY.add(y4);

        while (!isArithmeticProgression(topListOfY)) {
            count.addAndGet(1);
            var tempY1 = expression.substituteAndWorkOut(Map.of(v, x = x.add(BigDecimal.ONE))).toMathResult().getValue();

            var temp = tempY1;
            for (int i = 0; i < listOfLastY.size(); i++) {
                listOfLastY.set(i, tempY1 = tempY1.subtract(listOfLastY.get(i)));
            }
            listOfLastY.add(0,temp);

            topListOfY.add(tempY1);
            List<BigDecimal> tempList = new ArrayList<>();

            for (int i = 0; i < topListOfY.size() - 1; i++) {
                tempList.add(topListOfY.get(i + 1).subtract(topListOfY.get(i)));
            }

            topListOfY = tempList;
        }
        Logger.getLogger().log(expression,"是一个" + count + "次函数");
        return count.get();
    }

    public static int getFunctionTimes(@NotNull String expression) {
        return getFunctionTimes(new MathExpression(expression));
    }

    public static int getFunctionTimes(@NotNull MathExpression expression,long timeOut,@NotNull TimeUnit timeUnit) throws ExecutionException , TimeoutException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        AtomicInteger integer = new AtomicInteger();
        Future<Integer> submit = executorService.submit(() -> getFunctionTimes(expression));
        try {
            integer.set(submit.get(timeOut, timeUnit));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return integer.get();
    }

    public static boolean isFunction(@NotNull MathExpression expression) {
        return expression.getVariableCount() == 1;
    }

    @NotNull
    public static MathExpression simplify(@NotNull MathExpression expression) {
        if (!expression.hasVariable()) {
            return expression;
        }

        StringBuilder expressionString = new StringBuilder();
        Map<Character,MathExpression> map = expression.getVariables().stream().collect(
                Collectors.toMap(c->c, c-> MathExpression.of(BigDecimal.ZERO))
        );
        expression.getVariables().forEach(v->{
            map.put(v,new MathExpression(String.valueOf(v)));
            MathFunction mathFunction = MathFunctionUtil.createMathFunction(expression.substituteExpression(map));
            var it = mathFunction.getCoefficients().values().iterator();
            var size = mathFunction.getCoefficients().size();
            for (int i = size - 1; i > 0 && it.hasNext(); i--) {
                BigDecimal next = it.next();
                if(next.equals(BigDecimal.ZERO)){
                    continue;
                }
                expressionString
                        .append(next.equals(BigDecimal.ONE) ? "" : next + "*")
                        .append(v)
                        .append(i == 1 ? "" : "^" + i)
                        .append("+");
            }
            map.put(v,MathExpression.of(BigDecimal.ZERO));
        });
        BigDecimal constToken = expression.substituteExpression(map).workOut();
        expressionString.append(constToken.equals(BigDecimal.ZERO) ? "" : constToken);
        return new MathExpression(
                StringUtil.formatNumber(expressionString.toString())
        );
    }
}
