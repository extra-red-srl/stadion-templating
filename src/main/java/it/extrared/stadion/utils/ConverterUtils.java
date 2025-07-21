package it.extrared.stadion.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class ConverterUtils {

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
