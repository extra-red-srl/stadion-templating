package it.extrared.stadion.templating.directive;

/** A literal, it simply returns the value it holds. */
public class Literal implements TemplateDirective {

    private Object value;

    public Literal(Object value) {
        this.value = value;
    }

    @Override
    public Object run(Object object) {
        return value;
    }
}
