package it.extrared.stadion.log;

public class LogWriterFactoryImpl implements LogWriterFactory {

    @Override
    public LogWriter getLogger(Class<?> clazz) {
        return new LogWriterImpl(clazz);
    }
}
