package it.extrared.stadion.templating.directive.filter;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.utils.ConverterUtils;

public class LessThanEq extends BiNumeric {

    public LessThanEq(TemplateDirective left, TemplateDirective right) {
        super(left, right);
    }

    @Override
    public boolean evaluate(Object context) {
        Object left = getLeft().run(context);
        Object right = getRight().run(context);
        if (left == null || right == null) return false;
        left = ConverterUtils.convertValue(left, right.getClass());
        return asNumber(left).doubleValue() <= asNumber(right).doubleValue();
    }
}
