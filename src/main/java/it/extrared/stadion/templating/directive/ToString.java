package it.extrared.stadion.templating.directive;

import java.math.BigDecimal;
import java.util.List;

public class ToString extends FunctionDirective {
    private static final List<Class<?>> DECIMAL_CLASSES =
            List.of(Float.class, Double.class, BigDecimal.class);

    @Override
    public Object run(Object o) {
        if (o == null) return null;
        if (DECIMAL_CLASSES.stream().anyMatch(c -> c.isAssignableFrom(o.getClass()))) {
            double decimal = ((Number) o).floatValue();
            return String.format("%.2f", decimal);
        }
        return o.toString();
    }
}
