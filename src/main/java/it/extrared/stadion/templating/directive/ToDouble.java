package it.extrared.stadion.templating.directive;

import it.extrared.stadion.exceptions.TemplatingException;

public class ToDouble extends FunctionDirective {
    @Override
    public Object run(Object o) {
        return switch (o) {
            case null -> null;
            case Number n -> n.doubleValue();
            case String s -> Double.parseDouble(s);
            default ->
                    throw new TemplatingException(
                            "Cannot convert to double class %s".formatted(o.getClass().getName()));
        };
    }
}
