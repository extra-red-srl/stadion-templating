package it.extrared.stadion.templating.directive.property;

import java.beans.IntrospectionException;

/** Basic interface for a single part of a property. */
public interface PropertyPart {

    /**
     * Execute the property against the evaluation context.
     *
     * @param o the evaluation context.
     * @return the result of the evaluation.
     */
    Object evaluate(Object o) throws IntrospectionException;
}
