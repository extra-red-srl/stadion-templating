package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.DateFormatDirective;
import it.extrared.stadion.templating.directive.TemplateDirective;

public class DateTimeFormatDirectiveFactory extends AbstractDirectiveFactory {
    private static final DirectiveInfo INFO =
            new DirectiveInfo(
                    "dt_fmt",
                    new ParameterInfo(String.class, 0, false),
                    new ParameterInfo(String.class, 1, false));

    @Override
    protected TemplateDirective createInternal(Object... params) {
        return new DateFormatDirective(params[0].toString(), params[1].toString());
    }

    @Override
    public DirectiveInfo getInfo() {
        return INFO;
    }
}
