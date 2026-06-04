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

import it.extrared.stadion.templating.directive.Literal;
import it.extrared.stadion.templating.directive.TemplateDirective;

/** Parses inline interpolation expressions enclosed in double curly braces, e.g. {{property}}. */
public class InlineParser extends ChainingSingleDirectiveParser {

    private static final String INLINE_REGEX = "^\\$inline\\d+$";

    public static final String INLINE = "$inline";

    public InlineParser(SingleDirectiveParser next) {
        super(next);
    }

    @Override
    public TemplateDirective parse(String value) {
        if (value.matches(INLINE_REGEX)) {
            return new Literal(INLINE);
        }
        return super.parse(value);
    }
}
