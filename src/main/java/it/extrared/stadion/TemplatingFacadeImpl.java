package it.extrared.stadion;

import it.extrared.stadion.catalog.TemplateCatalog;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.ServiceNotFound;
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

public class TemplatingFacadeImpl<ID> implements TemplatingFacade<ID> {

    private final TemplateCatalog<ID> templateCatalog;

    public TemplatingFacadeImpl(TemplateCatalog<ID> templateCatalog) {
        this.templateCatalog =
                Objects.requireNonNull(templateCatalog, "templateCatalog must not be null");
    }

    @Override
    public void applyTemplate(
            ID templateId, MediaType outMediaType, OutputStream outputStream, InputData... input)
            throws ServiceNotFound, IOException, InvalidTemplateException {
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
                        ServiceProvider.getServiceByMediaType(
                                TemplateInputConverter.class, i.getMediaType());
                converted.add(ic.convert(i.getInput()));
            }
            template.exec(converted.size() == 1 ? converted.getFirst() : converted, outputWriter);
        } finally {
            for (InputData i : input) {
                if (i.getInput() != null) i.getInput().close();
            }
        }
    }

    @Override
    public void applyTemplate(
            ID id,
            MediaType targetMediaType,
            OutputStream outputStream,
            MediaType inputMediaType,
            InputStream input)
            throws ServiceNotFound, IOException, InvalidTemplateException {
        applyTemplate(
                id,
                targetMediaType,
                outputStream,
                InputData.builder().mediaType(inputMediaType).input(input).build());
    }

    @Override
    public void applyTemplateOnPojo(
            ID id, MediaType targetMediaType, OutputStream outputStream, Object input)
            throws ServiceNotFound, IOException, InvalidTemplateException {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(targetMediaType, "targetMediaType must not be null");
        Objects.requireNonNull(outputStream, "outputStream must not be null");
        Objects.requireNonNull(input, "input must not be null");
        StadionTemplate template = templateCatalog.loadTemplateById(id, targetMediaType);
        OutputWriterFactory factory =
                ServiceProvider.getServiceByMediaType(OutputWriterFactory.class, targetMediaType);
        try (OutputWriter outputWriter =
                factory.createOutputWriter(outputStream, template.getGlobalProperties())) {
            template.exec(input, outputWriter);
        }
    }
}
