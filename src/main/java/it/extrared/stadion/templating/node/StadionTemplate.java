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
package it.extrared.stadion.templating.node;

import it.extrared.stadion.write.OutputWriter;
import java.io.IOException;
import java.util.Map;

/**
 * A compiled, executable template.
 *
 * <p>Instances are produced by {@link it.extrared.stadion.read.TemplateReader} and are designed to
 * be shared across threads once compiled (e.g. via {@link
 * it.extrared.stadion.catalog.CachingTemplateCatalog}). The template tree itself is immutable; all
 * mutable execution state is confined to per-call {@link NodeExecutionContext} objects.
 */
public class StadionTemplate {

    private final Map<String, Object> globalProperties;
    private final TemplateNode templateNode;

    public StadionTemplate(TemplateNode templateNode, Map<String, Object> globalProperties) {
        this.globalProperties = globalProperties;
        this.templateNode = templateNode;
    }

    /**
     * Returns the global properties defined at the root of the template (e.g. XML namespace
     * declarations).
     *
     * @return an unmodifiable map of global properties
     */
    public Map<String, Object> getGlobalProperties() {
        return globalProperties;
    }

    /**
     * Evaluates the template against {@code value} and writes the result to {@code outputWriter}.
     *
     * @param value the root context object (POJO, {@link com.fasterxml.jackson.databind.JsonNode},
     *     {@link org.w3c.dom.Node}, or a {@link java.util.List} for composite input)
     * @param outputWriter the writer that serialises the output
     * @throws IOException on write errors
     */
    public void exec(Object value, OutputWriter outputWriter) throws IOException {
        templateNode.apply(value, outputWriter);
    }
}
