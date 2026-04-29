package it.extrared.stadion.templating.directive;

import it.extrared.stadion.templating.directive.parser.DirectiveParser;
import it.extrared.stadion.templating.directive.parser.FilterParser;

public class IfThenElseDirective extends FunctionDirective {

    private final TemplateDirective filter;
    private final TemplateDirective then;
    private final TemplateDirective elseF;

    public IfThenElseDirective(String filter, String then, String elseF) {
        DirectiveParser directiveParser = new DirectiveParser();
        this.filter = new FilterParser(null).parse(filter);
        this.then = directiveParser.parse(then);
        this.elseF = directiveParser.parse(elseF);
    }

    @Override
    public Object run(Object object) {
        if ((Boolean) this.filter.run(object)) {
            return then.run(object);
        }
        return elseF.run(object);
    }
}
