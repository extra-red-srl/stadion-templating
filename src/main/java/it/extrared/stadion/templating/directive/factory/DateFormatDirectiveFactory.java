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
package it.extrared.stadion.templating.directive.factory;

import it.extrared.stadion.templating.directive.DateFormatDirective;
import it.extrared.stadion.templating.directive.TemplateDirective;

/** Factory for DateFormatDirective instances. */
public class DateFormatDirectiveFactory extends AbstractDirectiveFactory {
    private static final DirectiveInfo INFO =
            new DirectiveInfo(
                    "d_fmt",
                    new ParameterInfo(String.class, 0, false),
                    new ParameterInfo(String.class, 1, false));

    @Override
    protected TemplateDirective createInternal(Object... params) {
        return new DateFormatDirective(params[0].toString(), params[1].toString());
    }

    @Override
    public DirectiveInfo getInfo() {
        return INFO;
    }
}
