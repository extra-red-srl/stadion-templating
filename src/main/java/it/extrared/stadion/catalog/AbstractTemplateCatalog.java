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

import it.extrared.stadion.ServiceProvider;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.exceptions.UnsupportedMediaTypeException;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.formats.TemplateType;
import it.extrared.stadion.read.TemplateReader;
import it.extrared.stadion.read.TemplateReaderFactory;
import it.extrared.stadion.templating.node.StadionTemplate;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base class for {@link TemplateCatalog} implementations that handle template loading and
 * validation logic common to all storage backends.
 *
 * <p>Subclasses must implement {@link #getTemplateStream(Object)} and {@link
 * #getTemplateStream(String, TemplateType)} to provide the raw bytes for a given template
 * identifier; this class takes care of selecting the correct {@link TemplateReader} via the SPI and
 * compiling the template tree.
 *
 * @param <ID> the type of the template identifier
 */
public abstract class AbstractTemplateCatalog<ID> implements TemplateCatalog<ID> {

    @Override
    public StadionTemplate loadTemplateByName(String templateName, MediaType mediaType)
            throws UnsupportedMediaTypeException, IOException {
        return loadTemplateInternal(
                getTemplateStream(templateName, TemplateType.getSupporting(mediaType)), mediaType);
    }

    @Override
    public StadionTemplate loadTemplateById(ID id, MediaType mediaType)
            throws UnsupportedMediaTypeException, IOException {
        return loadTemplateInternal(getTemplateStream(id), mediaType);
    }

    /**
     * Parses the template from {@code inputStream} using the {@link TemplateReader} registered for
     * {@code mediaType}.
     *
     * @param inputStream the raw template stream; must not be {@code null}
     * @param mediaType the output media type used to select the reader
     * @return the compiled template
     * @throws IOException if parsing fails, the stream is unreadable, or no reader is registered
     */
    protected StadionTemplate loadTemplateInternal(InputStream inputStream, MediaType mediaType)
            throws IOException {
        StadionTemplate template = null;
        try (TemplateReader templateReader =
                ServiceProvider.getServiceByMediaType(TemplateReaderFactory.class, mediaType)
                        .createReader(inputStream)) {
            template = templateReader.readTemplate();
        } catch (ServiceNotFound e) {
            throw new IOException(e);
        }
        if (template == null)
            throw new IOException(
                    "Loading of template for media type %s resulted in a null object."
                            .formatted(mediaType.asMime()));
        return template;
    }

    /**
     * Validates that the given metadata has a non-null, space-free name and a non-null template
     * type.
     *
     * @param metadata the metadata to validate
     * @throws InvalidTemplateException if any constraint is violated
     */
    protected void validate(TemplateMetadata<String> metadata) throws InvalidTemplateException {
        if (metadata.getName() == null)
            throw new InvalidTemplateException("Null template name is  not allowed.");
        if (metadata.getName().contains(" "))
            throw new InvalidTemplateException("Blank space are not allowed in template names.");
        if (metadata.getTemplateType() == null) {
            throw new InvalidTemplateException("Null template type is not allowed.");
        }
    }

    /**
     * Returns an {@link InputStream} for the template identified by {@code id}.
     *
     * @param id the template identifier
     * @return a stream over the raw template content; never {@code null}
     * @throws UnsupportedMediaTypeException if the template format is not supported
     * @throws IOException if the stream cannot be opened
     */
    protected abstract InputStream getTemplateStream(ID id)
            throws UnsupportedMediaTypeException, IOException;

    /**
     * Returns an {@link InputStream} for the template identified by name and type.
     *
     * @param templateName the logical template name (without extension)
     * @param templateType the template file format
     * @return a stream over the raw template content; never {@code null}
     * @throws UnsupportedMediaTypeException if the template format is not supported
     * @throws IOException if the stream cannot be opened
     */
    protected abstract InputStream getTemplateStream(String templateName, TemplateType templateType)
            throws UnsupportedMediaTypeException, IOException;
}
