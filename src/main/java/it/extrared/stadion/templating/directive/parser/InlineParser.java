package it.extrared.stadion.templating.directive.parser;

import it.extrared.stadion.templating.directive.Literal;
import it.extrared.stadion.templating.directive.TemplateDirective;

public class InlineParser extends ChainingSingleDirectiveParser {

    private static final String INLINE_REGEX = "^\\$inline\\d+$";

    public static final String INLINE = "$inline";

    public InlineParser(SingleDirectiveParser next) {
        super(next);
    }

    @Override
    public TemplateDirective parse(String value) {
        if (value.matches(INLINE_REGEX)) {
            return new Literal(INLINE);
        }
        return super.parse(value);
    }
}
