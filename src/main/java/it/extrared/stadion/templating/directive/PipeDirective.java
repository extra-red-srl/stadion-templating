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

/**
 * A directive representing a composition of directives through a pipeline eg {{first | second |
 * third}}
 */
public class PipeDirective extends CompositeDirective {

    public PipeDirective(TemplateDirective... directives) {
        super(directives);
    }

    @Override
    public Object run(Object object) {
        if (directives == null) return null;
        Object result = object;
        Class<?> type = object == null ? null : object.getClass();
        for (TemplateDirective directive : directives) {
            result = directive.run(result);
        }
        return result;
    }
}
