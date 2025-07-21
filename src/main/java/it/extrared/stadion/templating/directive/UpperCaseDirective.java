package it.extrared.stadion.templating.directive;

/** A directive that uppercase a string. */
public class UpperCaseDirective extends FunctionDirective {

    @Override
    public Object run(Object object) {
        return object == null ? null : object.toString().toUpperCase();
    }
}
