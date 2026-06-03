package it.extrared.stadion.integration.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.extrared.stadion.TemplatingFacade;
import it.extrared.stadion.TemplatingFacadeImpl;
import it.extrared.stadion.catalog.CachingTemplateCatalog;
import it.extrared.stadion.catalog.DirectoryTemplateCatalog;
import it.extrared.stadion.catalog.TemplateCatalog;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.formats.TemplateType;
import it.extrared.stadion.integration.StadionIntegrationTest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XmlOutputIntegrationTest extends StadionIntegrationTest {

    private static final XPath X_PATH = XPathFactory.newInstance().newXPath();

    @Test
    public void testXmlToXmlTemplating()
            throws InvalidTemplateException,
                    IOException,
                    ServiceNotFound,
                    XPathExpressionException,
                    ParserConfigurationException,
                    SAXException {
        String templateName = "testTemplate1";
        TemplateCatalog<String> templateCatalog =
                new CachingTemplateCatalog<>(new DirectoryTemplateCatalog(root));
        TemplatingFacade<String> facade = new TemplatingFacadeImpl<>(templateCatalog);
        String id = saveTemplate(templateCatalog, templateName, TemplateType.XML);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = getClass().getResourceAsStream("testPayload1.xml")) {
            facade.applyTemplate(id, MediaType.A_XML, baos, MediaType.A_XML, is);
        }
        byte[] bytes = baos.toByteArray();
        assertResult(bytes);
    }

    @Test
    public void testJsonToXmlTemplating()
            throws InvalidTemplateException,
                    IOException,
                    ServiceNotFound,
                    XPathExpressionException,
                    ParserConfigurationException,
                    SAXException {
        String templateName = "testTemplate2";
        TemplateCatalog<String> templateCatalog =
                new CachingTemplateCatalog<>(new DirectoryTemplateCatalog(root));
        TemplatingFacade<String> facade = new TemplatingFacadeImpl<>(templateCatalog);
        String id = saveTemplate(templateCatalog, templateName, TemplateType.XML);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = getClass().getResourceAsStream("testPayload1.json")) {
            facade.applyTemplate(id, MediaType.A_XML, baos, MediaType.A_JSON, is);
        }
        byte[] bytes = baos.toByteArray();
        assertResult(bytes);
    }

    private void assertResult(byte[] bytes)
            throws IOException,
                    ParserConfigurationException,
                    SAXException,
                    XPathExpressionException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder db = builderFactory.newDocumentBuilder();
        Document document = db.parse(new ByteArrayInputStream(bytes));
        assertEq("/Product/name/text()", "battery", document);
        assertEq(
                "/Product/manufacturer/location/text()",
                "via Salvo di Acquisto 44p Pisa IT",
                document);
        assertEq("/Product/manufacturer/name/text()", "Extrared", document);
        assertEq("/Product/materials/text()", "materials: lithium,nichel", document);
        assertEq("/Product/performance/performanceIndex/text()", 10, document);
        assertEq("/Product/performance/envPerformance/text()", 10.4d, document);
        assertEq("/Product/performance/recycled/text()", false, document);
        assertEq("/Product/productionDate/text()", "22/04/2024", document);
    }

    private void assertEq(String xpath, Object test, Node context) throws XPathExpressionException {
        Object result = X_PATH.compile(xpath).evaluate(context, XPathConstants.STRING);
        assertEquals(test.toString(), result);
    }
}
