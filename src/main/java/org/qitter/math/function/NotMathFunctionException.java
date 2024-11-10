package org.qitter.math.function;

import org.jetbrains.annotations.NotNull;

/**
 * 不是一个函数时抛出的异常
 */
public class NotMathFunctionException extends RuntimeException {
    public NotMathFunctionException(@NotNull String message) {
        super(message);
    }
    public NotMathFunctionException(@NotNull String message,@NotNull Throwable cause) {
        super(message,cause);
    }
}
