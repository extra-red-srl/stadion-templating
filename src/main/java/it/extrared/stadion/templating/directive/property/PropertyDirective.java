/*
 * Copyright 2026 Extrared
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.extrared.stadion.templating.directive.property;

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.templating.directive.TemplateDirective;
import java.util.LinkedList;

/**
 * {@link TemplateDirective} that traverses a dot-separated POJO property path via JavaBeans getter
 * introspection.
 *
 * <p>For example, the path {@code menu.entries.[0].name} is resolved by calling {@code getMenu()},
 * then {@code getEntries()}, then accessing element {@code 0}, then {@code getName()}.
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
