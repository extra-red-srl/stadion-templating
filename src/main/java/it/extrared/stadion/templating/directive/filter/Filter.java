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
package it.extrared.stadion.templating.directive.filter;

/**
 * Boolean predicate evaluated against a template execution context.
 *
 * <p>Filters are used in {@code $if} directives and inside {@code $if_then_else} expressions to
 * control conditional rendering. Negate any filter by prefixing it with {@code $!} in the template
 * (e.g. {@code $!eq}).
 *
 * @see it.extrared.stadion.templating.directive.IfThenElseDirective
 */
public interface Filter {

    /**
     * Evaluates this filter against the given context.
     *
     * @param context the current evaluation context (a POJO, {@link
     *     com.fasterxml.jackson.databind.JsonNode}, {@link org.w3c.dom.Node}, or a primitive value)
     * @return {@code true} if the condition is met
     */
    boolean evaluate(Object context);
}
