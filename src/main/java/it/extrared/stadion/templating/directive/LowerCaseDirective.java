package it.extrared.stadion.templating.directive;

/** A template directive that lowercase a string. */
public class LowerCaseDirective extends FunctionDirective {

    @Override
    public Object run(Object object) {
        return object == null ? null : object.toString().toLowerCase();
    }
}
