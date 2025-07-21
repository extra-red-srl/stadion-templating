package it.extrared.stadion.log;

import java.util.ServiceLoader;

public class LogWriters {

    public static LogWriter getLogger(Class<?> clazz) {
        ServiceLoader<LogWriterFactory> loader = ServiceLoader.load(LogWriterFactory.class);
        return loader.findFirst().orElse(new LogWriterFactoryImpl()).getLogger(clazz);
    }
}
