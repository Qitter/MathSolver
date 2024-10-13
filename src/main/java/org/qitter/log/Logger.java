package org.qitter.log;

import org.jetbrains.annotations.NotNull;

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
    @NotNull
    private final PrintWriter logWriter;
    @NotNull
    private final PrintWriter errorWriter;
    private boolean log;
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
        if(!log) {
            return;
        }
        logService.execute(() -> {
            logWriter.println(message);
            logWriter.flush();
        });
    }

    public void log(@NotNull CharSequence ...messages) {
        if(!log) {
            return;
        }
        log(String.join(" --- ", messages));
    }

    public void log(@NotNull Object o,CharSequence ... messages) {
        if(!log) {
            return;
        }
        log();
        log(Objects.toString(o),String.join(" --- ", messages));
        log();
    }

    public void log(@NotNull Object o) {
        if(!log) {
            return;
        }
        log(String.valueOf(o));
        log();
    }

    public void log(@NotNull Object o,@NotNull CharSequence message) {
        if(!log) {
            return;
        }
        log(o + " : " + message);
    }
    @NotNull
    public RuntimeException error(@NotNull RuntimeException e) {
        if(!log) {
            return e;
        }
        e.printStackTrace(errorWriter);
        errorWriter.flush();
        return e;
    }

    @Override
    public void close() {
        logService.shutdown();
        errorService.shutdown();
        logger.logWriter.close();
        logger.errorWriter.close();
    }
}
