package it.extrared.stadion.templating.directive;

import it.extrared.stadion.exceptions.TemplatingException;

public class ToInt extends FunctionDirective {
    @Override
    public Object run(Object o) {
        return switch (o) {
            case null -> null;
            case Number n -> n.intValue();
            case String s -> Integer.parseInt(s);
            default ->
                    throw new TemplatingException(
                            "Cannot convert to int class %s".formatted(o.getClass().getName()));
        };
    }
}
