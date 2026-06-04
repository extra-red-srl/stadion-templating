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

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.templating.directive.TemplateDirective;

/** Abstract binary string comparison Filter. */
public abstract class BiString extends BiFilter {

    protected BiString(TemplateDirective left, TemplateDirective right) {
        super(left, right);
    }

    protected String asString(Object str) {
        if (str != null && !(str instanceof String)) {
            throw new TemplatingException("String filters works only with string values");
        }
        return (String) str;
    }
}
