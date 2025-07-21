package it.extrared.stadion.templating.directive.property;

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.templating.directive.TemplateDirective;
import java.util.LinkedList;

/**
 * The PropertyName directive that uses reflection to interpolate a string against the pojo passed
 * as the evaluation context.
 */
public class PropertyDirective implements TemplateDirective {

    protected LinkedList<PropertyPart> propertyParts;

    public PropertyDirective(String propertyName) {
        this.propertyParts = PropertyPartsFactory.asParts(propertyName);
    }

    @Override
    public Object run(Object context) {
        try {
            Object result = context;
            for (PropertyPart part : propertyParts) {
                result = part.evaluate(result);
            }
            return result;
        } catch (Throwable t) {
            throw new TemplatingException(t);
        }
    }
}
