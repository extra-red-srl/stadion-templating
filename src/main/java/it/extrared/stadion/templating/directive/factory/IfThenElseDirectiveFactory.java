package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.IfThenElseDirective;
import it.extrared.stadion.templating.directive.TemplateDirective;

public class IfThenElseDirectiveFactory extends AbstractDirectiveFactory {
    private static final DirectiveInfo INFO =
            new DirectiveInfo(
                    "if_then_else",
                    new ParameterInfo(String.class, 0, false),
                    new ParameterInfo(String.class, 1, false),
                    new ParameterInfo(String.class, 2, false));

    @Override
    protected TemplateDirective createInternal(Object... params) {
        return new IfThenElseDirective(
                params[0].toString(), params[1].toString(), params[2].toString());
    }

    @Override
    public DirectiveInfo getInfo() {
        return INFO;
    }
}
