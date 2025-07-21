package it.extrared.stadion.catalog;

import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.UnsupportedMediaTypeException;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.templating.node.StadionTemplate;
import java.io.IOException;
import java.util.List;

public interface TemplateCatalog<ID> {

    StadionTemplate loadTemplateByName(String templateName, MediaType mediaType)
            throws InvalidTemplateException, IOException;

    StadionTemplate loadTemplateById(ID id, MediaType mediaType)
            throws UnsupportedMediaTypeException, IOException;

    byte[] loadTemplateContent(ID id);

    TemplateMetadata<ID> saveTemplate(byte[] content, TemplateMetadata<ID> metadata)
            throws InvalidTemplateException, IOException;

    TemplateMetadata<ID> findOne(ID id);

    List<TemplateMetadata<ID>> searchTemplates(SearchParams searchParams);
}
