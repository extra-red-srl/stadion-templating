package it.extrared.stadion.templating.directive;

import it.extrared.stadion.templating.directive.filter.Filter;

public class FilterTemplateDirective implements TemplateDirective {

    private Filter filter;

    public FilterTemplateDirective(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Boolean run(Object object) {
        return filter.evaluate(object);
    }
}
