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

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.templating.directive.factory.TemplateDirectiveFactory;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Registry of all {@link TemplateDirectiveFactory} instances discovered via {@link ServiceLoader}.
 *
 * <p>Factories are indexed by directive name at class-load time, enabling O(1) lookup. The
 * underlying map is immutable and the class has no mutable state, so instances are safe for
 * concurrent use.
 */
public class DirectiveProvider {

    /**
     * All directive factories indexed by name, loaded once via {@link ServiceLoader}. Lookup is
     * O(1) and the map is immutable after construction, making it safe for concurrent use.
     */
    private static final Map<String, TemplateDirectiveFactory> FACTORIES_BY_NAME;

    static {
        ServiceLoader<TemplateDirectiveFactory> loader =
                ServiceLoader.load(TemplateDirectiveFactory.class);
        FACTORIES_BY_NAME =
                StreamSupport.stream(loader.spliterator(), false)
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        f -> f.getInfo().getName(),
                                        f -> f,
                                        // keep first on name collision
                                        (a, b) -> a));
    }

    /**
     * Creates and returns a directive with the given name and parameters.
     *
     * @param name the directive name (e.g. {@code $lower}, {@code $d_fmt})
     * @param params the directive parameters
     * @return the constructed directive
     * @throws it.extrared.stadion.exceptions.TemplatingException if no factory is registered for
     *     {@code name}
     */
    public TemplateDirective createDirective(String name, Object... params) {
        TemplateDirectiveFactory factory = FACTORIES_BY_NAME.get(name);
        if (factory == null)
            throw new TemplatingException(
                    "No template directive found with name %s".formatted(name));
        return factory.createDirective(params);
    }

    /**
     * Returns an iterator over all registered {@link TemplateDirectiveFactory} instances. Used by
     * parsers to enumerate known directive names.
     *
     * @return an iterator over the registered factories
     */
    public Iterator<TemplateDirectiveFactory> spiIterator() {
        return FACTORIES_BY_NAME.values().iterator();
    }
}
