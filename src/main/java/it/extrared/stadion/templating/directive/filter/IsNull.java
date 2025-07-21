package it.extrared.stadion.templating.directive.filter;

import it.extrared.stadion.templating.directive.TemplateDirective;

public class IsNull implements Filter {

    private final TemplateDirective arg;

    public IsNull(TemplateDirective arg) {
        this.arg = arg;
    }

    @Override
    public boolean evaluate(Object context) {
        return arg.run(context) == null;
    }
}
