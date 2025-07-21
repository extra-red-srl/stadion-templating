package it.extrared.stadion.templating.directive.parser;

import it.extrared.stadion.templating.directive.TemplateDirective;

public abstract class ChainingSingleDirectiveParser implements SingleDirectiveParser {

    private SingleDirectiveParser next;

    protected ChainingSingleDirectiveParser(SingleDirectiveParser next) {
        this.next = next;
    }

    @Override
    public TemplateDirective parse(String value) {
        return next != null ? next.parse(value) : null;
    }
}
