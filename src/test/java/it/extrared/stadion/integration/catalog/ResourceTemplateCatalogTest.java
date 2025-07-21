package it.extrared.stadion.integration.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.extrared.stadion.catalog.*;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.formats.TemplateType;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ResourceTemplateCatalogTest {

    @Test
    public void testFindOne() throws IOException, InvalidTemplateException {
        String templateName = "testTemplate1";
        TemplateCatalog<String> templateCatalog = new ResourcesTemplateCatalog();
        TemplateMetadata<String> templateMetadata =
                templateCatalog.findOne(templateName.concat(".json"));
        assertEquals(templateName, templateMetadata.getName());
        assertEquals(templateName.concat(".json"), templateMetadata.getId());
        assertEquals(TemplateType.JSON, templateMetadata.getTemplateType());
    }

    @Test
    public void testSearch() throws IOException, InvalidTemplateException {
        String templateName = "testTemplate1";
        TemplateCatalog<String> templateCatalog = new ResourcesTemplateCatalog();
        SearchParams searchParams = new SearchParams();
        searchParams.setName("testTemplate1");
        searchParams.setTemplateType(TemplateType.XML);
        List<TemplateMetadata<String>> templates = templateCatalog.searchTemplates(searchParams);
        assertEquals(1, templates.size());
        TemplateMetadata<String> meta = templates.getFirst();
        assertEquals(templateName, meta.getName());
        assertEquals(TemplateType.XML, meta.getTemplateType());
        assertEquals(templateName.concat(".xml"), meta.getId());
    }
}
