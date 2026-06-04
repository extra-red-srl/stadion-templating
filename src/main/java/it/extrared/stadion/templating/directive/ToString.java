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

import java.math.BigDecimal;
import java.util.List;

/** Converts the evaluated value to its {@link String} representation. */
public class ToString extends FunctionDirective {
    private static final List<Class<?>> DECIMAL_CLASSES =
            List.of(Float.class, Double.class, BigDecimal.class);

    @Override
    public Object run(Object o) {
        if (o == null) return null;
        if (DECIMAL_CLASSES.stream().anyMatch(c -> c.isAssignableFrom(o.getClass()))) {
            double decimal = ((Number) o).floatValue();
            return String.format("%.2f", decimal);
        }
        return o.toString();
    }
}
