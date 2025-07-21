package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.LowerCaseDirective;
import it.extrared.stadion.templating.directive.TemplateDirective;

public class LowerCaseDirectiveFactory extends AbstractDirectiveFactory {

    private DirectiveInfo DIRECTIVE_INFO = new DirectiveInfo("lower");

    @Override
    public DirectiveInfo getInfo() {
        return DIRECTIVE_INFO;
    }

    @Override
    public TemplateDirective createInternal(Object... params) {
        return new LowerCaseDirective();
    }
}
