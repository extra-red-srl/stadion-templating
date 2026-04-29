package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.ThisTemplateDirective;

public class ThisDirectiveFactory extends AbstractDirectiveFactory {

    private static final DirectiveInfo INFO = new DirectiveInfo("this");

    @Override
    protected TemplateDirective createInternal(Object... params) {
        return new ThisTemplateDirective();
    }

    @Override
    public DirectiveInfo getInfo() {
        return INFO;
    }
}
