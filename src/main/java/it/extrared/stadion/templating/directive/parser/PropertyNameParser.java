package it.extrared.stadion.templating.directive.parser;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.property.PropertyDirective;

public class PropertyNameParser implements SingleDirectiveParser {
    @Override
    public TemplateDirective parse(String value) {
        return new PropertyDirective(value.replaceAll(" ", ""));
    }
}
