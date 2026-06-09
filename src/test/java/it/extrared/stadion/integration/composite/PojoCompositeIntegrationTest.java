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
package it.extrared.stadion.integration.composite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.extrared.stadion.TemplatingFacade;
import it.extrared.stadion.TemplatingFacadeImpl;
import it.extrared.stadion.catalog.CachingTemplateCatalog;
import it.extrared.stadion.catalog.DirectoryTemplateCatalog;
import it.extrared.stadion.catalog.TemplateCatalog;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.exceptions.UnsupportedInputTypeException;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.formats.TemplateType;
import it.extrared.stadion.input.InputData;
import it.extrared.stadion.integration.StadionIntegrationTest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for composite input that mixes a Java POJO with a JSON or XML source.
 *
 * <p>Each test verifies that {@code $isPojo}, {@code $isJSON}, and {@code $isXML} guards correctly
 * route template sections to the right source, and that inline merging produces a single flat
 * output object.
 */
public class PojoCompositeIntegrationTest extends StadionIntegrationTest {

    // ── POJO model ────────────────────────────────────────────────────────────

    public static class Product {
        private Long id;
        private String category;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    // ── fixtures ──────────────────────────────────────────────────────────────

    private TemplateCatalog<String> catalog;
    private TemplatingFacade<String> facade;
    private Product product;

    @BeforeEach
    public void setUp() {
        catalog = new CachingTemplateCatalog<>(new DirectoryTemplateCatalog(root));
        facade = new TemplatingFacadeImpl<>(catalog);

        product = new Product();
        product.setId(1L);
        product.setCategory("electronics");
    }

    // ── tests ─────────────────────────────────────────────────────────────────

    /**
     * POJO + JSON composite: fields from the POJO ({@code id}, {@code category}) and from the JSON
     * source ({@code name}, {@code price}) are merged into a single flat JSON object via {@code
     * $isPojo} and {@code $isJSON} inline guards.
     */
    @Test
    public void testPojoAndJsonComposite()
            throws InvalidTemplateException,
                    IOException,
                    ServiceNotFound,
                    UnsupportedInputTypeException {
        String id = saveTemplate(catalog, "testTemplatePojoJson", TemplateType.JSON);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = getClass().getResourceAsStream("testCompositeJsonPayload.json")) {
            facade.applyTemplate(
                    id,
                    MediaType.A_JSON,
                    baos,
                    InputData.pojoInputData(product),
                    InputData.jsonInputData(is));
        }
        assertCompositeResult(new ObjectMapper().readTree(baos.toByteArray()));
    }

    /**
     * POJO + XML composite: fields from the POJO ({@code id}, {@code category}) and from the XML
     * source ({@code name}, {@code price}) are merged into a single flat JSON object via {@code
     * $isPojo} and {@code $isXML} inline guards.
     */
    @Test
    public void testPojoAndXmlComposite()
            throws InvalidTemplateException,
                    IOException,
                    ServiceNotFound,
                    UnsupportedInputTypeException {
        String id = saveTemplate(catalog, "testTemplatePojoXml", TemplateType.JSON);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = getClass().getResourceAsStream("testCompositeXmlPayload.xml")) {
            facade.applyTemplate(
                    id,
                    MediaType.A_JSON,
                    baos,
                    InputData.pojoInputData(product),
                    InputData.xmlInputData(is));
        }
        assertCompositeResult(new ObjectMapper().readTree(baos.toByteArray()));
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void assertCompositeResult(JsonNode json) {
        JsonNode idNode = json.at("/id");
        assertFalse(idNode.isMissingNode(), "/id must be present");
        assertEquals(1L, idNode.asLong());

        JsonNode categoryNode = json.at("/category");
        assertFalse(categoryNode.isMissingNode(), "/category must be present");
        assertEquals("electronics", categoryNode.asText());

        JsonNode nameNode = json.at("/name");
        assertFalse(nameNode.isMissingNode(), "/name must be present");
        assertEquals("Laptop", nameNode.asText());

        JsonNode priceNode = json.at("/price");
        assertFalse(priceNode.isMissingNode(), "/price must be present");
        assertEquals(999.99, priceNode.asDouble(), 0.001);
    }
}
