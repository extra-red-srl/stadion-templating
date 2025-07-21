package it.extrared.stadion.templating.directive.filter;

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.templating.directive.TemplateDirective;

public abstract class BiString extends BiFilter {

    protected BiString(TemplateDirective left, TemplateDirective right) {
        super(left, right);
    }

    protected String asString(Object str) {
        if (str != null && !(str instanceof String)) {
            throw new TemplatingException("String filters works only with string values");
        }
        return (String) str;
    }
}
