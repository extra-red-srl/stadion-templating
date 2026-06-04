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

import it.extrared.stadion.templating.directive.parser.DirectiveParser;
import it.extrared.stadion.templating.directive.parser.SingleDirectiveParser;

/** Returns the {@code then} result when the filter passes, or the {@code else} result otherwise. */
public class IfThenElseDirective extends FunctionDirective {

    private final TemplateDirective filter;
    private final TemplateDirective then;
    private final TemplateDirective elseF;

    public IfThenElseDirective(String filter, String then, String elseF) {
        SingleDirectiveParser directiveParser = new DirectiveParser().getParserChain();
        this.filter = directiveParser.parse(filter);
        this.then = directiveParser.parse(then);
        this.elseF = directiveParser.parse(elseF);
    }

    @Override
    public Object run(Object object) {
        if ((Boolean) this.filter.run(object)) {
            return then.run(object);
        }
        return elseF.run(object);
    }
}
