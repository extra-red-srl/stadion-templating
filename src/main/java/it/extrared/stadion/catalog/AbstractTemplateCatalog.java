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

    protected void validate(TemplateMetadata<String> metadata) throws InvalidTemplateException {
        if (metadata.getName() == null)
            throw new InvalidTemplateException("Null template name is  not allowed.");
        if (metadata.getName().contains(" "))
            throw new InvalidTemplateException("Blank space are not allowed in template names.");
        if (metadata.getTemplateType() == null) {
            throw new InvalidTemplateException("Null template type is not allowed.");
        }
    }

    protected abstract InputStream getTemplateStream(ID id)
            throws UnsupportedMediaTypeException, IOException;

    protected abstract InputStream getTemplateStream(String templateName, TemplateType templateType)
            throws UnsupportedMediaTypeException, IOException;
}
