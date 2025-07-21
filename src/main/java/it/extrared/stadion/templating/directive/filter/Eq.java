package it.extrared.stadion.templating.directive.filter;

import it.extrared.stadion.templating.directive.TemplateDirective;
import java.util.Objects;

public class Eq implements Filter {

    private final TemplateDirective right;

    private final TemplateDirective left;

    public Eq(TemplateDirective right, TemplateDirective left) {
        this.right = right;
        this.left = left;
    }

    @Override
    public boolean evaluate(Object context) {
        return Objects.equals(right.run(context), left.run(context));
    }
}
