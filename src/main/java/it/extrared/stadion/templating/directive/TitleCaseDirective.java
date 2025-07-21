package it.extrared.stadion.templating.directive;

public class TitleCaseDirective extends FunctionDirective {

    @Override
    public Object run(Object object) {
        if (object == null) return null;
        String str = object.toString();
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
