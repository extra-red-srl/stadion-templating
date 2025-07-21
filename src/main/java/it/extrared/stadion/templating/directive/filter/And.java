package it.extrared.stadion.templating.directive.filter;

public class And extends BiOperator {

    public And(Filter left, Filter right) {
        super(left, right);
    }

    @Override
    public boolean evaluate(Object context) {
        return left.evaluate(context) && right.evaluate(context);
    }
}
