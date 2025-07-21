package it.extrared.stadion.templating.directive;

public class FilterAndDirective implements TemplateDirective {

    private TemplateDirective filter;

    private TemplateDirective directive;

    public FilterAndDirective(TemplateDirective filter, TemplateDirective directive) {
        this.filter = filter;
        this.directive = directive;
    }

    @Override
    public Object run(Object context) {
        return this.directive.run(context);
    }

    public TemplateDirective getFilter() {
        return filter;
    }

    public TemplateDirective getDirective() {
        return directive;
    }
}
