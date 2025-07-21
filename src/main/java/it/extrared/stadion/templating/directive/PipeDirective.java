package it.extrared.stadion.templating.directive;

/**
 * A directive representing a composition of directives through a pipeline eg {{first | second |
 * third}}
 */
public class PipeDirective extends CompositeDirective {

    public PipeDirective(TemplateDirective... directives) {
        super(directives);
    }

    @Override
    public Object run(Object object) {
        if (directives == null) return null;
        Object result = object;
        Class<?> type = object == null ? null : object.getClass();
        for (TemplateDirective directive : directives) {
            result = directive.run(result);
        }
        return result;
    }
}
