package it.extrared.stadion.templating.directive;

public class StrReplace extends FunctionDirective {

    private final String replaced;
    private final String replacement;

    public StrReplace(String replaced, String replacement) {
        this.replaced = replaced;
        this.replacement = replacement;
    }

    @Override
    public Object run(Object object) {
        if (object == null) return null;
        return object.toString().replace(replaced, replacement);
    }
}
