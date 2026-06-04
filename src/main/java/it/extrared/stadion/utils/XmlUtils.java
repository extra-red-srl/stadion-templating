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

import static it.extrared.stadion.utils.CommonUtils.unwrapWhiteSpace;

/** XML-specific conversion and formatting utilities. */
public class XmlUtils {

    private static final String BOOLEAN_LITERAL = "true|false";

    private static final String DOUBLE_LITERAL = "-?\\d+(\\.\\d+)?";

    private static final String INT_LITERAL = "-?\\d+";

    /**
     * Converts an XML text node value to the most specific Java type.
     *
     * <p>Returns a {@link Boolean} for {@code true}/{@code false}, a numeric type for digit
     * sequences, or the original string otherwise.
     *
     * @param text the raw text content
     * @return the typed value
     */
    public static Object covertText(String text) {
        String value = unwrapWhiteSpace(text);
        Object result = text;
        if (value != null) {
            if (value.matches(BOOLEAN_LITERAL)) {
                result = Boolean.valueOf(value);
            } else if (value.matches(INT_LITERAL)) {
                Long temp = Long.valueOf(value);
                if (temp <= Integer.MAX_VALUE) result = temp.intValue();
                else result = temp;
            } else if (value.matches(DOUBLE_LITERAL)) {
                result = Double.valueOf(value);
            }
        }
        return result;
    }
}
