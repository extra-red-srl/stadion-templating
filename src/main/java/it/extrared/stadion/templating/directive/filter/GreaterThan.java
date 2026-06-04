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

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.utils.ConverterUtils;

/** A Filter that checks a greater-than numeric comparison. */
public class GreaterThan extends BiNumeric {

    public GreaterThan(TemplateDirective left, TemplateDirective right) {
        super(left, right);
    }

    @Override
    public boolean evaluate(Object context) {
        Object left = getLeft().run(context);
        Object right = getRight().run(context);
        if (left == null || right == null) return false;
        left = ConverterUtils.convertValue(left, right.getClass());
        return asNumber(left).doubleValue() > asNumber(right).doubleValue();
    }
}
