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
import it.extrared.stadion.templating.directive.property.PropertyDirective;

/**
 * Terminal parser in the {@link SingleDirectiveParser} chain. Always succeeds by treating the input
 * as a POJO property name (possibly dot-separated path). Has no {@code next} parser since property
 * names are the catch-all fallback.
 */
public class PropertyNameParser extends ChainingSingleDirectiveParser {

    public PropertyNameParser() {
        super(null);
    }

    @Override
    public TemplateDirective parse(String value) {
        return new PropertyDirective(value.replaceAll(" ", ""));
    }
}
