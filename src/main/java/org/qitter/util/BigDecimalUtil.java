package org.qitter.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BigDecimalUtil {
    /**
     * 四舍五入
     * @param value 数值
     * @param scale 小数位数
     * @param mode 舍入模式
     * @return 舍后的数字
     */
    @NotNull
    public static BigDecimal setScale(@NotNull BigDecimal value, int scale,@NotNull RoundingMode mode) {
        return value.setScale(scale, mode).stripTrailingZeros();
    }

    /**
     * 检查是否为等差数列
     * @param list 数列
     * @return 是否为等差数列
     */
    public static boolean isArithmeticProgression(@NotNull final List<BigDecimal> list) {
        if(list.size() < 2) {
            return false;
        }
        if(list.size() == 2) {
            return true;
        }

        BigDecimal first = list.get(0);
        BigDecimal end = list.get(list.size() - 1);

        // ((first + end) * size ) / 2
        BigDecimal sum = first.add(end).multiply(BigDecimal.valueOf(list.size())).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        return sum.compareTo(list.stream().reduce(BigDecimal.ZERO, BigDecimal::add)) == 0;
//        return v <= 0.1 && v >= -0.1;
    }

    public static boolean isArithmeticProgression(@NotNull final BigDecimal... list) {
        return isArithmeticProgression(List.of(list));
    }

    @NotNull
    public static BigDecimal stripTrailingZeros(@NotNull BigDecimal b) {
        String exp = b.toString();
        int index = exp.indexOf(".");
        if(index == -1) {
            return b;
        }
        int end = exp.length() - 1;
        for (; end > index; end--) {
            char c = exp.charAt(end);
            if(c != '0') {
                break;
            }
        }

        return new BigDecimal(exp.substring(0, end + 1));
    }


}
