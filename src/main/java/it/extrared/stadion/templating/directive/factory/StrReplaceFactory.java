package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.StrReplace;
import it.extrared.stadion.templating.directive.TemplateDirective;

public class StrReplaceFactory extends AbstractDirectiveFactory {

    private static final DirectiveInfo DIRECTIVE_INFO =
            new DirectiveInfo(
                    "replace",
                    new ParameterInfo(String.class, 0),
                    new ParameterInfo(String.class, 1));

    @Override
    protected TemplateDirective createInternal(Object... objects) {
        return new StrReplace(objects[0].toString(), objects[1].toString());
    }

    @Override
    public DirectiveInfo getInfo() {
        return DIRECTIVE_INFO;
    }
}
