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

import static it.extrared.stadion.utils.CommonUtils.unwrapWhiteSpace;

import it.extrared.stadion.log.LogWriter;
import it.extrared.stadion.log.LogWriters;
import it.extrared.stadion.templating.directive.Literal;
import it.extrared.stadion.templating.directive.TemplateDirective;

/** Parses literal (constant) values within a directive expression. */
public class LiteralParser extends ChainingSingleDirectiveParser {

    private static final LogWriter LOG = LogWriters.getLogger(LiteralParser.class);

    private static final String STR_LITERAL = "'.*?'";

    private static final String BOOLEAN_LITERAL = "true|false";

    private static final String DOUBLE_LITERAL = "-?\\d+(\\.\\d+)?";

    private static final String INT_LITERAL = "-?\\d+";

    public LiteralParser(SingleDirectiveParser next) {
        super(next);
    }

    @Override
    public TemplateDirective parse(String value) {
        Object result = parseString(value);
        if (result == null && !value.equals("null")) {
            return super.parse(value);
        }
        LOG.debug("Parsing of %s resulted in a literal.".formatted(value));
        return new Literal(result);
    }

    private Object parseString(String value) {
        Object result = null;
        value = unwrapWhiteSpace(value);
        if (value.matches(BOOLEAN_LITERAL)) {
            result = Boolean.valueOf(value);
        } else if (value.matches(INT_LITERAL)) {
            Long temp = Long.valueOf(value);
            if (temp <= Integer.MAX_VALUE) result = temp.intValue();
            else result = temp;
        } else if (value.matches(DOUBLE_LITERAL)) {
            result = Double.valueOf(value);
        } else if (value.matches(STR_LITERAL)) {
            result = value.length() > 2 ? value.substring(1, value.length() - 1) : "";
        }
        return result;
    }
}
