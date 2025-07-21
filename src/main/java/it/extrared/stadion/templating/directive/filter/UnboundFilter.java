package it.extrared.stadion.templating.directive.filter;

import it.extrared.stadion.templating.directive.TemplateDirective;

abstract class UnboundFilter implements Filter {

    protected TemplateDirective[] directives;

    protected UnboundFilter(TemplateDirective... directives) {
        this.directives = directives;
    }
}
