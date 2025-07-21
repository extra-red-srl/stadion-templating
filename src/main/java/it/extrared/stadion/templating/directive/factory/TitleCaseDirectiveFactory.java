package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.TitleCaseDirective;

public class TitleCaseDirectiveFactory extends AbstractDirectiveFactory {

    private final DirectiveInfo DIRECTIVE_INFO = new DirectiveInfo("titlecase");

    @Override
    protected TemplateDirective createInternal(Object... params) {
        return new TitleCaseDirective();
    }

    @Override
    public DirectiveInfo getInfo() {
        return DIRECTIVE_INFO;
    }
}
