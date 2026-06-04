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
package it.extrared.stadion.templating.directive.parser;

import it.extrared.stadion.templating.directive.TemplateDirective;

/**
 * A single step in the directive parsing chain of responsibility.
 *
 * <p>Each implementation tries to parse the given expression and either returns a {@link
 * it.extrared.stadion.templating.directive.TemplateDirective TemplateDirective} or delegates to the
 * next parser in the chain.
 *
 * @see ChainingSingleDirectiveParser
 */
public interface SingleDirectiveParser {

    /**
     * Parses the given directive expression and returns the corresponding directive.
     *
     * @param value the raw expression string (without the surrounding {@code {{ }}})
     * @return the parsed directive
     */
    TemplateDirective parse(String value);
}
