package org.qitter.util;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtil {
    @NotNull
    public static BigDecimal setScale(@NotNull BigDecimal value, int scale,@NotNull RoundingMode mode) {
        return value.setScale(scale, mode).stripTrailingZeros();
    }
}
