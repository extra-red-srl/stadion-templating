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

import it.extrared.stadion.formats.MediaType;

/**
 * SPI marker interface for services that handle a specific set of {@link MediaType} values.
 *
 * <p>Implementations are discovered via {@link java.util.ServiceLoader} and ranked by {@link
 * #priority()} so that the most specific handler wins when multiple candidates support the same
 * media type.
 */
public interface MediaTypeHandlerService {

    /**
     * Returns the priority of this handler. Higher values take precedence when multiple handlers
     * support the same {@link MediaType}.
     *
     * @return a non-negative priority value
     */
    int priority();

    /**
     * Returns {@code true} if this handler can process the given media type.
     *
     * @param mediaType the media type to check
     * @return {@code true} if supported
     */
    boolean supportsMediaType(MediaType mediaType);
}
