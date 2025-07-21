package it.extrared.stadion.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import it.extrared.stadion.log.LogWriter;
import it.extrared.stadion.log.LogWriters;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Stream;

public class CommonUtils {

    private static final String DEF_DATE_FORMAT = "yyyy-MM-dd";

    private static final LogWriter LOG = LogWriters.getLogger(CommonUtils.class);

    public static boolean hasText(String astring) {
        return astring != null && astring.trim().length() > 0;
    }

    public static String defFormatDate(Date date) {
        return formatDate(DEF_DATE_FORMAT, date);
    }

    public static String formatDate(String format, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String defFormatTemporalAccessor(TemporalAccessor temporalAccessor) {
        return formatTemporalAccessor(DEF_DATE_FORMAT, temporalAccessor);
    }

    public static String formatTemporalAccessor(String format, TemporalAccessor temporalAccessor) {
        if (Instant.class.isAssignableFrom(temporalAccessor.getClass()))
            temporalAccessor =
                    ((Instant) temporalAccessor).atZone(ZoneOffset.UTC).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return formatter.format(TemporalAccessor.class.cast(temporalAccessor));
    }

    public static String unwrapWhiteSpace(String value) {
        int length = value.length();
        if (!hasText(value)) return value;
        while (value.lastIndexOf(' ') == length - 1) {
            value = value.substring(0, length - 1);
            length = value.length();
        }
        while (value.indexOf(' ') == 0) {
            value = value.substring(1);
        }
        return value;
    }

    /**
     * Try to cast the evaluation context to an iterator.
     *
     * @param o the evaluation context.
     * @return the evaluation context as a Collection if it is as such or null.
     */
    public static Iterator<?> tryIterator(Object o) {
        Iterator<?> result = null;
        if (o instanceof ArrayNode) result = ((ArrayNode) o).iterator();
        else if (o instanceof Collection<?>) result = ((Collection<?>) o).iterator();
        else if (o instanceof Iterator<?>) result = (Iterator<?>) o;
        else if (o != null && o.getClass().isArray()) result = Stream.of((Object[]) o).iterator();
        if (result != null) LOG.debug("The current evaluation context is a collection.");
        return result;
    }
}
