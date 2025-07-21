package it.extrared.stadion.templating.directive;

/** Basic interface for a template directive. */
public interface TemplateDirective {

    /**
     * Execute the directive against the evaluation context.
     *
     * @param object the evaluation context.
     * @return the result of the evaluation.
     */
    Object run(Object object);
}
