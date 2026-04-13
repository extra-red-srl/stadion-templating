package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.ToDouble;

public class ToDoubleFactory extends AbstractDirectiveFactory {

    private DirectiveInfo DIRECTIVE_INFO = new DirectiveInfo("double");

    @Override
    protected TemplateDirective createInternal(Object... objects) {
        return new ToDouble();
    }

    @Override
    public DirectiveInfo getInfo() {
        return DIRECTIVE_INFO;
    }
}
