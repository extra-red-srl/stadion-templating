package it.extrared.stadion.integration.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import it.extrared.stadion.TemplatingFacade;
import it.extrared.stadion.TemplatingFacadeImpl;
import it.extrared.stadion.catalog.CachingTemplateCatalog;
import it.extrared.stadion.catalog.DirectoryTemplateCatalog;
import it.extrared.stadion.catalog.TemplateCatalog;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.formats.TemplateType;
import it.extrared.stadion.input.InputData;
import it.extrared.stadion.integration.StadionIntegrationTest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

public class JsonOutputIntegrationTest extends StadionIntegrationTest {

    @Test
    public void testJsonToJsonTemplating()
            throws InvalidTemplateException, IOException, ServiceNotFound {
        String templateName = "testTemplate1";
        TemplateCatalog<String> templateCatalog =
                new CachingTemplateCatalog<>(new DirectoryTemplateCatalog(root));
        TemplatingFacade<String> facade = new TemplatingFacadeImpl<>(templateCatalog);
        String id = saveTemplate(templateCatalog, templateName, TemplateType.JSON);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = getClass().getResourceAsStream("testPayload1.json")) {
            facade.applyTemplate(id, MediaType.A_JSON, baos, MediaType.A_JSON, is);
        }
        byte[] bytes = baos.toByteArray();
        assertResult(bytes);
    }

    @Test
    public void testXmlToJsonTemplating()
            throws InvalidTemplateException, IOException, ServiceNotFound {
        String templateName = "testTemplate2";
        TemplateCatalog<String> templateCatalog =
                new CachingTemplateCatalog<>(new DirectoryTemplateCatalog(root));
        TemplatingFacade<String> facade = new TemplatingFacadeImpl<>(templateCatalog);
        String id = saveTemplate(templateCatalog, templateName, TemplateType.JSON);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = getClass().getResourceAsStream("testPayload1.xml")) {
            facade.applyTemplate(id, MediaType.A_JSON, baos, MediaType.A_XML, is);
        }
        byte[] bytes = baos.toByteArray();
        assertResult(bytes);
    }

    @Test
    public void testCompositeToJsonTemplating()
            throws InvalidTemplateException, IOException, ServiceNotFound {
        String templateName = "testTemplateComposite";
        TemplateCatalog<String> templateCatalog =
                new CachingTemplateCatalog<>(new DirectoryTemplateCatalog(root));
        TemplatingFacade<String> facade = new TemplatingFacadeImpl<>(templateCatalog);
        String id = saveTemplate(templateCatalog, templateName, TemplateType.JSON);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = getClass().getResourceAsStream("testCompositePayload.xml");
                InputStream is2 = getClass().getResourceAsStream("testCompositePayload.json")) {
            InputData inputData = InputData.builder().mediaType(MediaType.A_XML).input(is).build();
            InputData inputData2 =
                    InputData.builder().mediaType(MediaType.A_JSON).input(is2).build();
            facade.applyTemplate(id, MediaType.A_JSON, baos, inputData, inputData2);
        }
        byte[] bytes = baos.toByteArray();
        assertResult(bytes);
    }

    private void assertResult(byte[] bytes) throws IOException {
        JsonNode json = new ObjectMapper().readTree(bytes);
        JsonNode test = json.at("/name");
        assertFalse(test.isNull());
        assertEquals("battery", test.asText());
        test = json.at("/manufacturer/contactDetails/address");
        assertFalse(test.isNull());
        assertEquals("via Salvo di Acquisto 44p", test.asText());
        test = json.at("/manufacturer/contactDetails/city");
        assertFalse(test.isNull());
        assertEquals("Pisa", test.asText());
        test = json.at("/manufacturer/contactDetails/country");
        assertFalse(test.isNull());
        assertEquals("IT", test.asText());
        test = json.at("/manufacturer/name");
        assertFalse(test.isNull());
        assertEquals("Extrared", test.asText());
        test = json.at("/anode");
        assertFalse(test.isNull());
        ArrayNode materials = (ArrayNode) test;
        assertEquals(1, materials.size());
        String testName = "nichel - Ni";
        assertEquals(testName, materials.get(0).get("materialName").asText());
        assertTrue(materials.get(0).get("concentration").asDouble() >= 1.0);
        test = json.at("/performance/performanceIndex");
        assertFalse(test.isNull());
        assertEquals(10, test.asInt());
        test = json.at("/performance/envPerformance");
        assertFalse(test.isNull());
        assertEquals(10.4, test.asDouble());
        test = json.at("/performance/recycled");
        assertFalse(test.isNull());
        assertFalse(test.asBoolean());
        test = json.at("/productionDate");
        assertFalse(test.isNull());
        assertEquals("2024-04-22", test.asText());
    }
}
