package it.extrared.stadion.templating.directive;

public class ThisTemplateDirective extends FunctionDirective {

    @Override
    public Object run(Object object) {
        return object;
    }
}
