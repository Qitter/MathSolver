package org.qitter.log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Logger {
    private static final Path LOG_PATH = Path.of("logs.log");
    private static final Path ERROR_PATH = Path.of("errors.log");
    private static final ExecutorService logService = Executors.newSingleThreadExecutor();
    private static final ExecutorService errorService = Executors.newSingleThreadExecutor();
    private static final Logger logger = new Logger();
    private final PrintWriter logWriter;
    private final PrintWriter errorWriter;
    private Logger() {
        try {
            logWriter = new PrintWriter(LOG_PATH.toFile());
            errorWriter = new PrintWriter(ERROR_PATH.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Logger getLogger() {
        return logger;
    }

    public void log(@NotNull CharSequence message) {
        logService.execute(() -> {
            logWriter.println(message);
            logWriter.flush();
        });
    }

    public void log(@NotNull CharSequence ...messages) {
        log(String.join(" --- ", messages));
    }

    public void log(@NotNull Object o,CharSequence ... messages) {
        log();
        log(Objects.toString(o),String.join(" --- ", messages));
        log();
    }

    public void log(@NotNull Object o) {
        log(String.valueOf(o));
        log();
    }

    public void log(@NotNull Object o,@NotNull CharSequence message) {
        log(o + " : " + message);
    }

    public void error(@NotNull CharSequence message) {
        errorService.execute(() ->{
            errorWriter.println(message);
            errorWriter.flush();
        });
    }

    @NotNull
    public RuntimeException error(@NotNull RuntimeException e) {
        e.printStackTrace(errorWriter);
        return e;
    }

    public static void close() {
        logService.shutdown();
        errorService.shutdown();
        logger.logWriter.close();
        logger.errorWriter.close();
    }
}
