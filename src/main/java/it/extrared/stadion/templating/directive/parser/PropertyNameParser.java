package it.extrared.stadion.templating.directive.parser;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.property.PropertyDirective;

/**
 * Terminal parser in the {@link SingleDirectiveParser} chain. Always succeeds by treating the input
 * as a POJO property name (possibly dot-separated path). Has no {@code next} parser since property
 * names are the catch-all fallback.
 */
public class PropertyNameParser extends ChainingSingleDirectiveParser {

    public PropertyNameParser() {
        super(null);
    }

    @Override
    public TemplateDirective parse(String value) {
        return new PropertyDirective(value.replaceAll(" ", ""));
    }
}
