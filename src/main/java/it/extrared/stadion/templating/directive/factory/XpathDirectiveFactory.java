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

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.XpathDirective;

/** Factory for XpathDirective instances. */
public class XpathDirectiveFactory extends AbstractDirectiveFactory {

    private static final DirectiveInfo INFO =
            new DirectiveInfo("xpath", new ParameterInfo(String.class, 0, false));

    @Override
    public DirectiveInfo getInfo() {
        return INFO;
    }

    @Override
    public TemplateDirective createInternal(Object... params) {
        validate(params);
        String path = (String) params[0];
        return new XpathDirective(path);
    }
}
