package it.extrared.stadion.templating.directive;

/** Abstraction of a directive composed by several directives. */
public abstract class CompositeDirective implements TemplateDirective {

    protected TemplateDirective[] directives;

    protected CompositeDirective(TemplateDirective... directives) {
        this.directives = directives;
    }

    public TemplateDirective[] getDirectives() {
        return directives;
    }

    public CompositeDirective setDirectives(TemplateDirective[] directives) {
        this.directives = directives;
        return this;
    }
}
