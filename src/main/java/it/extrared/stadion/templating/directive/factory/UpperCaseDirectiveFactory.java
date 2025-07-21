package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.UpperCaseDirective;

public class UpperCaseDirectiveFactory extends AbstractDirectiveFactory {

    private final DirectiveInfo DIRECTIVE_INFO = new DirectiveInfo("upper");

    @Override
    public DirectiveInfo getInfo() {
        return DIRECTIVE_INFO;
    }

    @Override
    public TemplateDirective createInternal(Object... params) {
        return new UpperCaseDirective();
    }
}
