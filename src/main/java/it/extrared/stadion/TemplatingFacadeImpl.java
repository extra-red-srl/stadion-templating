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

import it.extrared.stadion.catalog.TemplateCatalog;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.exceptions.UnsupportedInputTypeException;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.input.InputData;
import it.extrared.stadion.input.TemplateInputConverter;
import it.extrared.stadion.templating.node.StadionTemplate;
import it.extrared.stadion.write.OutputWriter;
import it.extrared.stadion.write.OutputWriterFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Default implementation of {@link TemplatingFacade}.
 *
 * <p>Delegates template loading to the injected {@link TemplateCatalog}, discovers input converters
 * and output writer factories via {@link ServiceProvider}, and orchestrates the execution pipeline.
 *
 * @param <ID> the type of the template identifier
 */
public class TemplatingFacadeImpl<ID> implements TemplatingFacade<ID> {

    private final TemplateCatalog<ID> templateCatalog;

    public TemplatingFacadeImpl(TemplateCatalog<ID> templateCatalog) {
        this.templateCatalog =
                Objects.requireNonNull(templateCatalog, "templateCatalog must not be null");
    }

    @Override
    public void applyTemplate(
            ID templateId, MediaType outMediaType, OutputStream outputStream, InputData... input)
            throws ServiceNotFound,
                    IOException,
                    InvalidTemplateException,
                    UnsupportedInputTypeException {
        Objects.requireNonNull(templateId, "templateId must not be null");
        Objects.requireNonNull(outMediaType, "outMediaType must not be null");
        Objects.requireNonNull(outputStream, "outputStream must not be null");
        Objects.requireNonNull(input, "input must not be null");
        StadionTemplate template = templateCatalog.loadTemplateById(templateId, outMediaType);
        OutputWriterFactory factory =
                ServiceProvider.getServiceByMediaType(OutputWriterFactory.class, outMediaType);
        try (OutputWriter outputWriter =
                factory.createOutputWriter(outputStream, template.getGlobalProperties())) {
            List<Object> converted = new ArrayList<>();
            for (InputData i : input) {
                TemplateInputConverter ic =
                        ServiceProvider.getServiceByInputType(
                                TemplateInputConverter.class, i.getInputType());
                converted.add(ic.convert(i.getInput()));
            }
            template.exec(converted.size() == 1 ? converted.getFirst() : converted, outputWriter);
        } finally {
            for (InputData i : input) {
                if (i.getInput() != null && i.getInput() instanceof InputStream is) is.close();
            }
        }
    }
}
