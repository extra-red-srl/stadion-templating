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
package it.extrared.stadion;

import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.exceptions.UnsupportedInputTypeException;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.input.InputData;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Entry point for applying templates to input data and writing the result to an output stream.
 *
 * <p>A {@code TemplatingFacade} is parameterised by the type of template identifier used by the
 * underlying {@link it.extrared.stadion.catalog.TemplateCatalog TemplateCatalog} (typically {@link
 * String}).
 *
 * @param <ID> the type of the template identifier
 */
public interface TemplatingFacade<ID> {

    /**
     * Applies the template identified by {@code id} to one or more input sources and writes the
     * result in the requested format.
     *
     * @param id the template identifier
     * @param targetMediaType the desired output format (e.g. {@code MediaType.A_JSON})
     * @param outputStream the stream to write the rendered output to
     * @param inputs one or more input data sources; composite templates require more than one
     * @throws ServiceNotFound if no handler is registered for the requested media type
     * @throws IOException on I/O errors reading inputs or writing output
     * @throws InvalidTemplateException if the template is structurally invalid
     */
    void applyTemplate(
            ID id, MediaType targetMediaType, OutputStream outputStream, InputData... inputs)
            throws ServiceNotFound,
                    IOException,
                    InvalidTemplateException,
                    UnsupportedInputTypeException;
}
