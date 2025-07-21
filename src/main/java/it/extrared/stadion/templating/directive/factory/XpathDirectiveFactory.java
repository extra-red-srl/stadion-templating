package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.XpathDirective;

public class XpathDirectiveFactory extends AbstractDirectiveFactory {

    private static final DirectiveInfo INFO =
            new DirectiveInfo("xpath", new ParameterInfo(String.class, 0, false));

    @Override
    public DirectiveInfo getInfo() {
        return INFO;
    }

    @Override
    public TemplateDirective createInternal(Object... params) {
        validate(params);
        String path = (String) params[0];
        return new XpathDirective(path);
    }
}
