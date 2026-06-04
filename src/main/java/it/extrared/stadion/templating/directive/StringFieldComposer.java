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

import java.util.Collection;

/**
 * A directive that compose a string that has some static and some dynamic parts (result of a
 * directive evaluation).
 */
public class StringFieldComposer extends CompositeDirective {

    private String formattingString;

    public StringFieldComposer(String formattingString, TemplateDirective... directives) {
        super(directives);
        this.formattingString = formattingString;
    }

    @Override
    public Object run(Object context) {
        Object[] replacing = new String[directives.length];
        for (int i = 0; i < directives.length; i++) {
            TemplateDirective dir = directives[i];
            Object res = dir.run(context);
            replacing[i] = getString(res);
        }
        return formattingString.formatted(replacing);
    }

    private String getString(Object res) {
        StringBuilder result = new StringBuilder();
        if (res instanceof Collection<?>) {
            for (Object o : (Collection<?>) res) result.append(o.toString()).append(",");
            result.setLength(result.length() - 1);
        } else {
            result.append(res != null ? res.toString() : "");
        }
        return result.toString();
    }
}
