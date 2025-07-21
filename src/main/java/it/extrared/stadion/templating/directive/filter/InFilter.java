package it.extrared.stadion.templating.directive.filter;

import it.extrared.stadion.templating.directive.TemplateDirective;
import java.util.Objects;

public class InFilter extends UnboundFilter {

    private TemplateDirective left;

    public InFilter(TemplateDirective left, TemplateDirective[] directives) {
        super(directives);
        this.left = left;
    }

    @Override
    public boolean evaluate(Object context) {
        Object left = this.left.run(context);
        for (TemplateDirective dir : directives) {
            if (Objects.equals(left, dir.run(context))) return true;
        }
        return false;
    }
}
