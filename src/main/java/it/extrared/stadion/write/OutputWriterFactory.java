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
package it.extrared.stadion.write;

import it.extrared.stadion.MediaTypeHandlerService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * SPI factory for creating {@link OutputWriter} instances.
 *
 * <p>Implementations are discovered at runtime via {@link java.util.ServiceLoader} and selected
 * based on the output {@link it.extrared.stadion.formats.MediaType} using {@link
 * it.extrared.stadion.ServiceProvider}.
 */
public interface OutputWriterFactory extends MediaTypeHandlerService {

    /**
     * Creates an output writer that serialises template results to {@code outputStream}.
     *
     * @param outputStream the target stream
     * @param globalProperties global template properties (e.g. XML namespace declarations)
     * @return a ready-to-use output writer
     * @throws IOException if the writer cannot be initialised
     */
    OutputWriter createOutputWriter(OutputStream outputStream, Map<String, Object> globalProperties)
            throws IOException;
}
