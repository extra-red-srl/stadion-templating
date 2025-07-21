package it.extrared.stadion.templating.directive.filter;

abstract class BiOperator implements Filter {

    protected Filter left;

    protected Filter right;

    protected BiOperator(Filter left, Filter right) {
        this.left = left;
        this.right = right;
    }
}
