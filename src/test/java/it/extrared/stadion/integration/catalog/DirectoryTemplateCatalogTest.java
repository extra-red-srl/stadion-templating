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
package it.extrared.stadion.integration.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.extrared.stadion.catalog.DirectoryTemplateCatalog;
import it.extrared.stadion.catalog.SearchParams;
import it.extrared.stadion.catalog.TemplateCatalog;
import it.extrared.stadion.catalog.TemplateMetadata;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.formats.TemplateType;
import it.extrared.stadion.integration.StadionIntegrationTest;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DirectoryTemplateCatalogTest extends StadionIntegrationTest {

    @Test
    public void testFindOne() throws IOException, InvalidTemplateException {
        String templateName = "testTemplate1";
        TemplateCatalog<String> templateCatalog = new DirectoryTemplateCatalog(root);
        String id = saveTemplate(templateCatalog, templateName, TemplateType.JSON);
        TemplateMetadata<String> templateMetadata = templateCatalog.findOne(id);
        assertEquals(templateName, templateMetadata.getName());
        assertEquals(id, templateMetadata.getId());
        assertEquals(TemplateType.JSON, templateMetadata.getTemplateType());
    }

    @Test
    public void testSearch() throws IOException, InvalidTemplateException {
        String templateName = "testTemplate1";
        TemplateCatalog<String> templateCatalog = new DirectoryTemplateCatalog(root);
        saveTemplate(templateCatalog, templateName, TemplateType.JSON);
        String id2 = saveTemplate(templateCatalog, templateName, TemplateType.XML);
        SearchParams searchParams = new SearchParams();
        searchParams.setName("testTemplate1");
        searchParams.setTemplateType(TemplateType.XML);
        List<TemplateMetadata<String>> templates = templateCatalog.searchTemplates(searchParams);
        assertEquals(1, templates.size());
        TemplateMetadata<String> meta = templates.getFirst();
        assertEquals(templateName, meta.getName());
        assertEquals(TemplateType.XML, meta.getTemplateType());
        assertEquals(id2, meta.getId());
    }

    @Override
    protected byte[] resourceAsBytes(String resName) throws IOException {
        try (InputStream is =
                getClass().getResourceAsStream("/stadion-templates/".concat(resName))) {
            if (is != null) {
                return is.readAllBytes();
            }
        }
        throw new IOException("No bytes found");
    }
}
