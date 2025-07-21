package it.extrared.stadion.log;

public interface LogWriterFactory {

    LogWriter getLogger(Class<?> clazz);
}
