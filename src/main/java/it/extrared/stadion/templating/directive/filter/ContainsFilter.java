package it.extrared.stadion.templating.directive.filter;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.utils.ConverterUtils;

public class ContainsFilter extends BiString {
    public ContainsFilter(TemplateDirective left, TemplateDirective right) {
        super(left, right);
    }

    @Override
    public boolean evaluate(Object context) {
        Object one = left.run(context);
        Object two = right.run(context);
        String str1 = asString(ConverterUtils.convertValue(one, String.class));
        String str2 = asString(two);
        if (str1 == null || str2 == null) return false;
        return str1.contains(str2);
    }
}
