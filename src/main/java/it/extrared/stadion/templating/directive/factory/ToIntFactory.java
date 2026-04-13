package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.ToInt;

public class ToIntFactory extends AbstractDirectiveFactory {

    private DirectiveInfo DIRECTIVE_INFO = new DirectiveInfo("int");

    @Override
    protected TemplateDirective createInternal(Object... objects) {
        return new ToInt();
    }

    @Override
    public DirectiveInfo getInfo() {
        return DIRECTIVE_INFO;
    }
}
