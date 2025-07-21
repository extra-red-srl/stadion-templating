package it.extrared.stadion.templating.directive.filter;

public class Not implements Filter {

    private final Filter arg;

    public Not(Filter arg) {
        this.arg = arg;
    }

    @Override
    public boolean evaluate(Object context) {
        return !arg.evaluate(context);
    }
}
