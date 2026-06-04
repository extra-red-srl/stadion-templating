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
package it.extrared.stadion.templating.directive;

import it.extrared.stadion.templating.directive.filter.Filter;

/**
 * Evaluates a {@link it.extrared.stadion.templating.directive.filter.Filter} against the context
 * and returns a boolean result.
 */
/**
 * Evaluates a {@link it.extrared.stadion.templating.directive.filter.Filter} against the context
 * and returns a boolean result.
 */
public class FilterTemplateDirective implements TemplateDirective {

    private Filter filter;

    public FilterTemplateDirective(Filter filter) {
        this.filter = filter;
    }

    @Override
    public Boolean run(Object object) {
        return filter.evaluate(object);
    }
}
