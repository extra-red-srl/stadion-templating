package it.extrared.stadion.templating.directive.filter;

import it.extrared.stadion.templating.directive.TemplateDirective;

abstract class BiFilter implements Filter {

    protected TemplateDirective right;

    protected TemplateDirective left;

    protected BiFilter(TemplateDirective left, TemplateDirective right) {
        this.left = left;
        this.right = right;
    }

    protected TemplateDirective getRight() {
        return right;
    }

    protected TemplateDirective getLeft() {
        return left;
    }
}
