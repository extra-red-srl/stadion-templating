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
package it.extrared.stadion.input;

import it.extrared.stadion.exceptions.UnsupportedInputTypeException;
import it.extrared.stadion.formats.MediaType;
import java.io.IOException;
import java.io.InputStream;

/**
 * SPI converter that transforms a raw {@link InputStream} into a context object suitable for
 * template evaluation.
 *
 * <p>Implementations are discovered via {@link java.util.ServiceLoader}:
 *
 * <ul>
 *   <li>{@code JsonInputConverter} — parses JSON to a {@link
 *       com.fasterxml.jackson.databind.JsonNode}
 *   <li>{@code XmlInputConverter} — parses XML to a {@link org.w3c.dom.Document}
 * </ul>
 */
public interface TemplateInputConverter {

    /**
     * Parses the given input stream and returns the corresponding context object.
     *
     * @param input the input object
     * @return the parsed context object
     * @throws IOException if the stream cannot be parsed
     */
    Object convert(Object input) throws UnsupportedInputTypeException, IOException;

    /**
     * Returns the priority of this handler. Higher values take precedence when multiple handlers
     * support the same {@link MediaType}.
     *
     * @return a non-negative priority value
     */
    int priority();

    /**
     * Returns {@code true} if this handler can process the given input type.
     *
     * @param inputType the input type to check
     * @return {@code true} if supported
     */
    boolean supportsInputType(InputType inputType);
}
