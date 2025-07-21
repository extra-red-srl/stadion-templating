package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.TemplateDirective;

/** SPI factory for a template directive. */
public interface TemplateDirectiveFactory {

    /**
     * @return the directive metadata.
     */
    DirectiveInfo getInfo();

    /**
     * Create a directive out of a list of params.
     *
     * @param params the parameters to create a directive.
     * @return the template directive.
     */
    TemplateDirective createDirective(Object... params);
}
