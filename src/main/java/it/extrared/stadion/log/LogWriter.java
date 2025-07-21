package it.extrared.stadion.log;

import java.util.function.Supplier;

public interface LogWriter {

    void info(String message);

    void info(String message, Exception e);

    void info(Supplier<String> message);

    void info(Supplier<String> message, Exception e);

    void debug(String message);

    void debug(String message, Exception e);

    void debug(Supplier<String> message);

    void debug(Supplier<String> message, Exception e);

    void warn(String message);

    void warn(String message, Exception e);

    void warn(Supplier<String> message);

    void warn(Supplier<String> message, Exception e);

    void error(String message);

    void error(String message, Exception e);

    void error(String message, Throwable e);

    void error(Supplier<String> message);

    void error(Supplier<String> message, Exception e);

    void error(Supplier<String> message, Throwable e);

    void trace(String message);

    void trace(String message, Exception e);

    void trace(Supplier<String> message);

    void trace(Supplier<String> message, Exception e);
}
