package it.extrared.stadion.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import it.extrared.stadion.log.LogWriter;
import it.extrared.stadion.log.LogWriters;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class CommonUtils {

    private static final String DEF_DATE_FORMAT = "yyyy-MM-dd";

    private static final LogWriter LOG = LogWriters.getLogger(CommonUtils.class);

    /**
     * Cache of pre-built {@link DateTimeFormatter} instances keyed by pattern string. {@link
     * DateTimeFormatter} is immutable and thread-safe, so a single instance per pattern is safe to
     * share across all threads. Package-visible so {@link DateTimeUtils} can reuse it.
     */
    static final ConcurrentHashMap<String, DateTimeFormatter> FORMATTER_CACHE =
            new ConcurrentHashMap<>();

    static DateTimeFormatter getFormatter(String pattern) {
        return FORMATTER_CACHE.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
    }

    public static boolean hasText(String astring) {
        return astring != null && astring.trim().length() > 0;
    }

    public static String defFormatDate(Date date) {
        return formatDate(DEF_DATE_FORMAT, date);
    }

    public static String formatDate(String format, Date date) {
        return getFormatter(format).format(date.toInstant().atZone(ZoneOffset.UTC));
    }

    public static String defFormatTemporalAccessor(TemporalAccessor temporalAccessor) {
        return formatTemporalAccessor(DEF_DATE_FORMAT, temporalAccessor);
    }

    public static String formatTemporalAccessor(String format, TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Instant instant)
            temporalAccessor = instant.atZone(ZoneOffset.UTC).toLocalDateTime();
        return getFormatter(format).format(temporalAccessor);
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
    public static Collection<?> tryCollection(Object o) {
        Collection<?> result = null;
        if (o instanceof ArrayNode an) result = arrayNodeView(an);
        else if (o instanceof Collection<?> c) result = c;
        else if (o instanceof Iterator<?> it) result = toList(it);
        else if (o != null && o.getClass().isArray()) result = Stream.of((Object[]) o).toList();
        if (result != null) {
            LOG.debug("The current evaluation context is a collection.");
        }
        return result;
    }

    /**
     * Returns a live, read-only {@link List} view over an {@link ArrayNode} without copying its
     * elements. Random access is O(1) via {@link ArrayNode#get(int)}.
     */
    private static List<?> arrayNodeView(ArrayNode an) {
        return new AbstractList<Object>() {
            @Override
            public Object get(int index) {
                return an.get(index);
            }

            @Override
            public int size() {
                return an.size();
            }
        };
    }

    public static List<?> toList(Iterator<?> iterator) {
        List<Object> list = new ArrayList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }
}
