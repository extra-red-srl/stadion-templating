package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.ToString;

public class ToStringFactory extends AbstractDirectiveFactory {

    private DirectiveInfo DIRECTIVE_INFO = new DirectiveInfo("string");

    @Override
    protected TemplateDirective createInternal(Object... objects) {
        return new ToString();
    }

    @Override
    public DirectiveInfo getInfo() {
        return DIRECTIVE_INFO;
    }
}
