package it.extrared.stadion.templating.directive.parser;

import it.extrared.stadion.templating.directive.TemplateDirective;

public interface SingleDirectiveParser {

    TemplateDirective parse(String value);
}
