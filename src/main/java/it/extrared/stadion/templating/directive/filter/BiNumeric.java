package it.extrared.stadion.templating.directive.filter;

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.templating.directive.TemplateDirective;

public abstract class BiNumeric extends BiFilter {

    protected BiNumeric(TemplateDirective left, TemplateDirective right) {
        super(left, right);
    }

    protected Number asNumber(Object number) {
        if (number != null && !(number instanceof Number)) {
            throw new TemplatingException("Numeric filter works only with numeric values");
        }
        return (Number) number;
    }
}
