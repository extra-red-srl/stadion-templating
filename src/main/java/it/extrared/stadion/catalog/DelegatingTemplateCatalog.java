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
