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
package it.extrared.stadion.formats;

import it.extrared.stadion.exceptions.UnsupportedMediaTypeException;
import java.util.Arrays;

/**
 * The format of a template file.
 *
 * <p>Each constant is compatible with the media types whose sub-type string contains the constant
 * name in lower case (e.g. {@code JSON} matches {@code application/json}, {@code
 * application/ld+json}; {@code XML} matches {@code application/xml}, {@code text/xml}).
 */
public enum TemplateType {
    /** JSON-based template format. */
    JSON,
    /** XML-based template format. */
    XML;

    /**
     * Returns {@code true} if this template type is compatible with the given media type.
     *
     * @param mediaType the media type to check
     * @return {@code true} if compatible
     */
    public boolean isCompatibleWith(MediaType mediaType) {
        return mediaType.getSubType().contains(name().toLowerCase());
    }

    /**
     * Returns the first {@code TemplateType} that is compatible with {@code mediaType}.
     *
     * @param mediaType the media type to match
     * @return the compatible template type
     * @throws UnsupportedMediaTypeException if no template type supports the given media type
     */
    public static TemplateType getSupporting(MediaType mediaType)
            throws UnsupportedMediaTypeException {
        return Arrays.stream(values())
                .filter(v -> v.isCompatibleWith(mediaType))
                .findFirst()
                .orElseThrow(
                        () ->
                                new UnsupportedMediaTypeException(
                                        "MediaType %s has no supported template type."
                                                .formatted(mediaType.asMime())));
    }
}
