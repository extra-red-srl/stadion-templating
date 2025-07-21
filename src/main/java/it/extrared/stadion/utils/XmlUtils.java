package it.extrared.stadion.utils;

import static it.extrared.stadion.utils.CommonUtils.unwrapWhiteSpace;

public class XmlUtils {

    private static final String BOOLEAN_LITERAL = "true|false";

    private static final String DOUBLE_LITERAL = "-?\\d+(\\.\\d+)?";

    private static final String INT_LITERAL = "-?\\d+";

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
