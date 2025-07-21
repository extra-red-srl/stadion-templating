package it.extrared.stadion.log;

import static java.util.logging.Level.FINE;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogWriterImpl implements LogWriter {

    private Logger log;

    public LogWriterImpl(Class<?> clazz) {
        log = Logger.getLogger(clazz.getName());
    }

    @Override
    public void info(String message) {
        doLog(INFO, message);
    }

    @Override
    public void info(String message, Exception e) {
        doLog(INFO, message, e);
    }

    @Override
    public void info(Supplier<String> message) {
        doLog(INFO, message);
    }

    @Override
    public void info(Supplier<String> message, Exception e) {
        doLog(INFO, message, e);
    }

    @Override
    public void debug(String message) {
        doLog(FINE, message);
    }

    @Override
    public void debug(String message, Exception e) {
        doLog(FINE, message, e);
    }

    @Override
    public void debug(Supplier<String> message) {
        doLog(FINE, message);
    }

    @Override
    public void debug(Supplier<String> message, Exception e) {
        doLog(FINE, message, e);
    }

    @Override
    public void warn(String message) {
        doLog(WARNING, message);
    }

    @Override
    public void warn(String message, Exception e) {
        doLog(WARNING, message, e);
    }

    @Override
    public void warn(Supplier<String> message) {
        doLog(WARNING, message);
    }

    @Override
    public void warn(Supplier<String> message, Exception e) {
        doLog(WARNING, message, e);
    }

    @Override
    public void error(String message) {
        doLog(SEVERE, message);
    }

    @Override
    public void error(String message, Exception e) {
        doLog(SEVERE, message, e);
    }

    @Override
    public void error(Supplier<String> message) {
        doLog(SEVERE, message);
    }

    @Override
    public void error(Supplier<String> message, Exception e) {
        doLog(SEVERE, message, e);
    }

    @Override
    public void error(String message, Throwable e) {
        doLog(SEVERE, () -> message, e);
    }

    @Override
    public void error(Supplier<String> message, Throwable e) {
        doLog(SEVERE, message, e);
    }

    @Override
    public void trace(String message) {
        doLog(FINEST, message);
    }

    @Override
    public void trace(String message, Exception e) {
        doLog(FINEST, message, e);
    }

    @Override
    public void trace(Supplier<String> message) {
        doLog(FINEST, message);
    }

    @Override
    public void trace(Supplier<String> message, Exception e) {
        doLog(FINEST, message, e);
    }

    private void doLog(Level level, String message) {
        if (log.isLoggable(level)) log.log(level, message);
    }

    private void doLog(Level level, String message, Exception e) {
        if (log.isLoggable(level)) log.log(level, message, e);
    }

    private void doLog(Level level, Supplier<String> message) {
        if (log.isLoggable(level)) log.log(level, message);
    }

    private void doLog(Level level, Supplier<String> message, Exception e) {
        if (log.isLoggable(level)) log.log(level, message.get(), e);
    }

    private void doLog(Level level, Supplier<String> message, Throwable e) {
        if (log.isLoggable(level)) log.log(level, message.get(), e);
    }
}
