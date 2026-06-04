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
package it.extrared.stadion.catalog;

import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.UnsupportedMediaTypeException;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.templating.node.StadionTemplate;
import java.io.IOException;
import java.util.List;

abstract class DelegatingTemplateCatalog<ID> implements TemplateCatalog<ID> {

    private final TemplateCatalog<ID> delegate;

    public DelegatingTemplateCatalog(TemplateCatalog<ID> delegate) {
        this.delegate = delegate;
    }

    @Override
    public StadionTemplate loadTemplateById(ID id, MediaType mediaType)
            throws UnsupportedMediaTypeException, IOException {
        return delegate.loadTemplateById(id, mediaType);
    }

    @Override
    public StadionTemplate loadTemplateByName(String templateName, MediaType mediaType)
            throws InvalidTemplateException, IOException {
        return delegate.loadTemplateByName(templateName, mediaType);
    }

    @Override
    public TemplateMetadata<ID> saveTemplate(byte[] content, TemplateMetadata<ID> metadata)
            throws InvalidTemplateException, IOException {
        return delegate.saveTemplate(content, metadata);
    }

    @Override
    public TemplateMetadata<ID> findOne(ID id) {
        return delegate.findOne(id);
    }

    @Override
    public List<TemplateMetadata<ID>> searchTemplates(SearchParams searchParams) {
        return delegate.searchTemplates(searchParams);
    }

    @Override
    public byte[] loadTemplateContent(ID id) {
        return delegate.loadTemplateContent(id);
    }
}
