package it.extrared.stadion.utils;

import it.extrared.stadion.log.LogWriter;
import it.extrared.stadion.log.LogWriters;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    private static final LogWriter LOG = LogWriters.getLogger(DateTimeUtils.class);

    public static LocalDate parseDate(String value, String fmt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt);
            return LocalDate.parse(value, formatter);
        } catch (Throwable t) {
            String msg = "Error while parsing %s as %s".formatted(value, fmt);
            LOG.error(msg, t);
            throw new RuntimeException(msg);
        }
    }

    public static String formatDate(LocalDate date, String fmt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt);
            return formatter.format(date);
        } catch (Throwable t) {
            String msg = "Error while formatting date to %s".formatted(fmt);
            LOG.error(msg, t);
            throw new RuntimeException(msg);
        }
    }

    public static LocalDateTime parseDateTime(String value, String fmt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt);
            return LocalDateTime.parse(value, formatter);
        } catch (Throwable t) {
            String msg = "Error while formatting %s as %s".formatted(value, fmt);
            LOG.error(msg, t);
            throw new RuntimeException(msg);
        }
    }

    public static String formatDateTime(LocalDateTime date, String fmt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fmt);
            return formatter.format(date);
        } catch (Throwable t) {
            String msg = "Error while formatting date time as %s".formatted(fmt);
            LOG.error(msg, t);
            throw new RuntimeException(msg);
        }
    }
}
