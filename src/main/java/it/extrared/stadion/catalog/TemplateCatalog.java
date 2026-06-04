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

/**
 * Storage and retrieval abstraction for compiled {@link StadionTemplate} objects.
 *
 * <p>Implementations are provided for common storage backends:
 *
 * <ul>
 *   <li>{@link DirectoryTemplateCatalog} — loads templates from a filesystem directory
 *   <li>{@link ResourcesTemplateCatalog} — loads templates from the classpath
 *   <li>{@link CachingTemplateCatalog} — decorator that caches compiled templates in memory
 * </ul>
 *
 * @param <ID> the type of the template identifier (typically {@link String})
 */
public interface TemplateCatalog<ID> {

    /**
     * Loads and compiles a template by its logical name and the requested output media type.
     *
     * @param templateName the logical name of the template (without extension)
     * @param mediaType the output media type, used to select the template file format
     * @return the compiled template
     * @throws InvalidTemplateException if the template content is invalid
     * @throws IOException on I/O errors
     */
    StadionTemplate loadTemplateByName(String templateName, MediaType mediaType)
            throws InvalidTemplateException, IOException;

    /**
     * Loads and compiles a template by its unique identifier.
     *
     * @param id the template identifier (e.g. filename)
     * @param mediaType the output media type
     * @return the compiled template
     * @throws UnsupportedMediaTypeException if the media type is not supported
     * @throws IOException on I/O errors
     */
    StadionTemplate loadTemplateById(ID id, MediaType mediaType)
            throws UnsupportedMediaTypeException, IOException;

    /**
     * Returns the raw bytes of the template identified by {@code id} without compiling it.
     *
     * @param id the template identifier
     * @return the raw template content
     * @throws TemplateCatalogException if the template cannot be found or read
     */
    byte[] loadTemplateContent(ID id);

    /**
     * Persists a new template in the catalog.
     *
     * @param content the raw template bytes
     * @param metadata the metadata describing the template (name, type)
     * @return the saved metadata, enriched with the generated identifier
     * @throws InvalidTemplateException if the metadata is invalid
     * @throws IOException on I/O errors
     */
    TemplateMetadata<ID> saveTemplate(byte[] content, TemplateMetadata<ID> metadata)
            throws InvalidTemplateException, IOException;

    /**
     * Returns the metadata for the template identified by {@code id}.
     *
     * @param id the template identifier
     * @return the template metadata
     * @throws TemplateCatalogException if no template is found for the given identifier
     */
    TemplateMetadata<ID> findOne(ID id);

    /**
     * Searches for templates matching the given parameters.
     *
     * @param searchParams the search criteria (name, type)
     * @return a list of matching template metadata; never {@code null}
     */
    List<TemplateMetadata<ID>> searchTemplates(SearchParams searchParams);
}
