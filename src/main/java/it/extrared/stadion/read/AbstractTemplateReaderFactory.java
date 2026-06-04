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
package it.extrared.stadion.read;

import it.extrared.stadion.formats.MediaType;
import java.util.List;

abstract class AbstractTemplateReaderFactory implements TemplateReaderFactory {

    private List<MediaType> supportedMediaTypes;

    public AbstractTemplateReaderFactory(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    @Override
    public int priority() {
        return 99;
    }

    @Override
    public boolean supportsMediaType(MediaType mediaType) {
        return supportedMediaTypes.contains(mediaType);
    }
}
