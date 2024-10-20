package org.qitter.log;

import org.jetbrains.annotations.NotNull;
import org.qitter.Main;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Logger implements AutoCloseable {
    @NotNull
    private static final Path LOG_PATH = Path.of("logs.log");
    @NotNull
    private static final Path ERROR_PATH = Path.of("errors.log");
    @NotNull
    private static final ExecutorService logService = Executors.newSingleThreadExecutor();
    @NotNull
    private static final ExecutorService errorService = Executors.newSingleThreadExecutor();
    @NotNull
    private static final Logger logger = new Logger();
    private boolean opening = true;
    @NotNull
    private final PrintWriter logWriter;
    @NotNull
    private final PrintWriter errorWriter;
    private boolean log = true;
    private Logger() {
        try {
            logWriter = new PrintWriter(LOG_PATH.toFile());
            errorWriter = new PrintWriter(ERROR_PATH.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    @NotNull
    public static Logger getLogger() {
        return logger;
    }

    public void log(@NotNull CharSequence message) {
        if(canNotLog()) {
            return;
        }
        logService.execute(() -> {
            logWriter.println(message);
            logWriter.flush();
        });
    }

    public void log(@NotNull CharSequence ...messages) {
        if(canNotLog()) {
            return;
        }
        log(String.join(" --- ", messages));
    }

    public void log(@NotNull Object o,CharSequence ... messages) {
        if(canNotLog()) {
            return;
        }
        log();
        log(Objects.toString(o),String.join(" --- ", messages));
        log();
    }

    public void log(@NotNull Object o) {
        if(canNotLog()) {
            return;
        }
        log(String.valueOf(o));
        log();
    }

    public void log(@NotNull Object o,@NotNull CharSequence message) {
        if(canNotLog()) {
            return;
        }
        log(o + " : " + message);
    }

    private boolean canNotLog() {
        return !(log && opening);
    }

    /**
     * 将错误信息打印到错误流中，并关闭所有的文件读取器，防止因意外退出导致配置文件不全
     * @param e 错误信息
     * @return 错误信息，将实参返回，方便调用者处理
     */
    @NotNull
    public RuntimeException errorAndClose(@NotNull RuntimeException e) {
        if(canNotLog()) {
            return e;
        }
        e.printStackTrace(errorWriter);
        errorWriter.flush();
        Main.closeAll();
        return e;
    }

    /**
     * 将错误信息打印到错误流中，并执行指定的操作
     * 若捕获异常后仍需要继续运行，调用此方法，为防止调用此方法后忘记执行对应的操作，添加了参数
     * @param e 错误信息
     * @param execute 执行的操作
     * @return 错误信息，将实参返回，方便调用者处理
     */
    @NotNull
    public RuntimeException errorExecute(@NotNull RuntimeException e, @NotNull Runnable execute) {
        if(canNotLog()) {
            return e;
        }
        e.printStackTrace(errorWriter);
        errorWriter.flush();
        execute.run();
        return e;
    }

    @Override
    public void close() {
        opening = false;
        log("日志输出流已关闭");
        logService.shutdown();
        errorService.shutdown();
        logger.logWriter.close();
        logger.errorWriter.close();
    }
}
