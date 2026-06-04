/*
 * Copyright 2026 Extrared
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

/** General-purpose utility methods shared across the library. */
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

    /**
     * Returns {@code true} if {@code astring} is neither {@code null} nor blank.
     *
     * @param astring the string to test
     * @return {@code true} when the string contains at least one non-whitespace character
     */
    public static boolean hasText(String astring) {
        return astring != null && astring.trim().length() > 0;
    }

    /**
     * Formats {@code date} using the library default date pattern ({@code yyyy-MM-dd}).
     *
     * @param date the date to format
     * @return the formatted string
     */
    public static String defFormatDate(Date date) {
        return formatDate(DEF_DATE_FORMAT, date);
    }

    /**
     * Formats {@code date} using the given pattern.
     *
     * @param format the pattern string (e.g. {@code "dd/MM/yyyy"})
     * @param date the date to format
     * @return the formatted string
     */
    public static String formatDate(String format, Date date) {
        return getFormatter(format).format(date.toInstant().atZone(ZoneOffset.UTC));
    }

    /**
     * Formats a {@link java.time.temporal.TemporalAccessor} using the library default date pattern.
     *
     * @param temporalAccessor the temporal value to format
     * @return the formatted string
     */
    public static String defFormatTemporalAccessor(TemporalAccessor temporalAccessor) {
        return formatTemporalAccessor(DEF_DATE_FORMAT, temporalAccessor);
    }

    /**
     * Formats a {@link java.time.temporal.TemporalAccessor} using the given pattern.
     *
     * @param format the pattern string
     * @param temporalAccessor the temporal value to format
     * @return the formatted string
     */
    public static String formatTemporalAccessor(String format, TemporalAccessor temporalAccessor) {
        if (temporalAccessor instanceof Instant instant)
            temporalAccessor = instant.atZone(ZoneOffset.UTC).toLocalDateTime();
        return getFormatter(format).format(temporalAccessor);
    }

    /**
     * Trims leading and trailing spaces from {@code value}.
     *
     * @param value the string to trim
     * @return the trimmed string, or the original if empty
     */
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
