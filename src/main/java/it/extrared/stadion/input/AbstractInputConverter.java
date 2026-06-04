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

import it.extrared.stadion.formats.MediaType;
import java.util.List;

/**
 * Base implementation of {@link TemplateInputConverter} that delegates media-type compatibility
 * checks to a fixed list of supported types.
 */
public abstract class AbstractInputConverter implements TemplateInputConverter {

    private List<MediaType> supportedMediaTypes;

    protected AbstractInputConverter(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    @Override
    public boolean supportsMediaType(MediaType mediaType) {
        return supportedMediaTypes.contains(mediaType);
    }
}
