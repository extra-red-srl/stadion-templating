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

import it.extrared.stadion.log.LogWriter;
import it.extrared.stadion.log.LogWriters;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Date and time parsing/formatting utilities used by template directives. */
public class DateTimeUtils {

    private static final LogWriter LOG = LogWriters.getLogger(DateTimeUtils.class);

    /**
     * Parses {@code value} as a {@link LocalDate} using the given format pattern.
     *
     * @param value the string to parse
     * @param fmt the format pattern
     * @return the parsed date
     * @throws RuntimeException if parsing fails
     */
    public static LocalDate parseDate(String value, String fmt) {
        try {
            return LocalDate.parse(value, CommonUtils.getFormatter(fmt));
        } catch (Throwable t) {
            String msg = "Error while parsing %s as %s".formatted(value, fmt);
            LOG.error(msg, t);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Formats {@code date} using the given pattern.
     *
     * @param date the date to format
     * @param fmt the format pattern
     * @return the formatted string
     * @throws RuntimeException if formatting fails
     */
    public static String formatDate(LocalDate date, String fmt) {
        try {
            return CommonUtils.getFormatter(fmt).format(date);
        } catch (Throwable t) {
            String msg = "Error while formatting date to %s".formatted(fmt);
            LOG.error(msg, t);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Parses {@code value} as a {@link LocalDateTime} using the given format pattern.
     *
     * @param value the string to parse
     * @param fmt the format pattern
     * @return the parsed date-time
     * @throws RuntimeException if parsing fails
     */
    public static LocalDateTime parseDateTime(String value, String fmt) {
        try {
            return LocalDateTime.parse(value, CommonUtils.getFormatter(fmt));
        } catch (Throwable t) {
            String msg = "Error while formatting %s as %s".formatted(value, fmt);
            LOG.error(msg, t);
            throw new RuntimeException(msg);
        }
    }

    /**
     * Formats {@code date} as a date-time string using the given pattern.
     *
     * @param date the date-time to format
     * @param fmt the format pattern
     * @return the formatted string
     * @throws RuntimeException if formatting fails
     */
    public static String formatDateTime(LocalDateTime date, String fmt) {
        try {
            return CommonUtils.getFormatter(fmt).format(date);
        } catch (Throwable t) {
            String msg = "Error while formatting date time as %s".formatted(fmt);
            LOG.error(msg, t);
            throw new RuntimeException(msg);
        }
    }
}
