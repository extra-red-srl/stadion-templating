package it.extrared.stadion.templating.directive;

import java.util.Collection;

/**
 * A directive that compose a string that has some static and some dynamic parts (result of a
 * directive evaluation).
 */
public class StringFieldComposer extends CompositeDirective {

    private String formattingString;

    public StringFieldComposer(String formattingString, TemplateDirective... directives) {
        super(directives);
        this.formattingString = formattingString;
    }

    @Override
    public Object run(Object context) {
        Object[] replacing = new String[directives.length];
        for (int i = 0; i < directives.length; i++) {
            TemplateDirective dir = directives[i];
            Object res = dir.run(context);
            replacing[i] = getString(res);
        }
        return formattingString.formatted(replacing);
    }

    private String getString(Object res) {
        StringBuilder result = new StringBuilder();
        if (res instanceof Collection<?>) {
            for (Object o : (Collection<?>) res) result.append(o.toString()).append(",");
            result.setLength(result.length() - 1);
        } else {
            result.append(res != null ? res.toString() : "");
        }
        return result.toString();
    }
}
