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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

/** Type-conversion utilities used during template execution. */
public class ConverterUtils {

    /**
     * Converts {@code src} to an instance of {@code trgClass}.
     *
     * <p>Supported conversions: any type → {@link String}; {@link String} → numeric types and
     * {@link Boolean}. Returns {@code src} unchanged when the classes are the same or both are
     * {@link Number} subtypes.
     *
     * @param src the source value (may be {@code null})
     * @param trgClass the target type
     * @return the converted value, or {@code null} if {@code src} is {@code null}
     * @throws RuntimeException if no conversion is defined for the given pair
     */
    public static Object convertValue(Object src, Class<?> trgClass) {
        if (src == null) return null;
        Class<?> srcClass = src.getClass();
        if (trgClass == null
                || Objects.equals(srcClass, trgClass)
                || (Number.class.isAssignableFrom(srcClass)
                        && Number.class.isAssignableFrom(trgClass))) return src;

        if (String.class.equals(trgClass)) return src.toString();
        else if (String.class.isAssignableFrom(srcClass) && Number.class.isAssignableFrom(trgClass))
            return strToNumber(src.toString(), trgClass);
        else if (String.class.isAssignableFrom(srcClass)
                && Boolean.class.isAssignableFrom(trgClass)) return strToBoolean(src.toString());
        else
            throw new RuntimeException(
                    "Unable to convert %s to %s"
                            .formatted(srcClass.getSimpleName(), trgClass.getSimpleName()));
    }

    private static Boolean strToBoolean(String value) {
        return Boolean.valueOf(value);
    }

    private static Number strToNumber(String value, Class<?> trgClass) {
        Number result = null;
        if (Integer.class.isAssignableFrom(trgClass)) result = Long.parseLong(value);
        else if (Double.class.isAssignableFrom(trgClass)) result = Double.parseDouble(value);
        else if (Float.class.isAssignableFrom(trgClass)) result = Float.parseFloat(value);
        else if (Long.class.isAssignableFrom(trgClass)) result = Long.parseLong(value);
        else if (BigDecimal.class.isAssignableFrom(trgClass)) result = new BigDecimal(value);
        else if (BigInteger.class.isAssignableFrom(trgClass)) result = new BigInteger(value);
        else result = Short.parseShort(value);
        return result;
    }
}
