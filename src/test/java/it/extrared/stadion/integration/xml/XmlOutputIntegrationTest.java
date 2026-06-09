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
package it.extrared.stadion.integration.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlOutputIntegrationTest extends StadionIntegrationTest {

    @Test
    public void testXmlToXmlTemplating()
            throws InvalidTemplateException,
                    IOException,
                    ServiceNotFound,
                    XPathExpressionException,
                    ParserConfigurationException,
                    SAXException,
                    UnsupportedInputTypeException {
        String templateName = "testTemplate1";
        TemplateCatalog<String> templateCatalog =
                new CachingTemplateCatalog<>(new DirectoryTemplateCatalog(root));
        TemplatingFacade<String> facade = new TemplatingFacadeImpl<>(templateCatalog);
        String id = saveTemplate(templateCatalog, templateName, TemplateType.XML);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = getClass().getResourceAsStream("testPayload1.xml")) {
            facade.applyTemplate(id, MediaType.A_XML, baos, InputData.xmlInputData(is));
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
                    SAXException,
                    UnsupportedInputTypeException {
        String templateName = "testTemplate2";
        TemplateCatalog<String> templateCatalog =
                new CachingTemplateCatalog<>(new DirectoryTemplateCatalog(root));
        TemplatingFacade<String> facade = new TemplatingFacadeImpl<>(templateCatalog);
        String id = saveTemplate(templateCatalog, templateName, TemplateType.XML);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = getClass().getResourceAsStream("testPayload1.json")) {
            facade.applyTemplate(id, MediaType.A_XML, baos, InputData.jsonInputDate(is));
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
        assertLengthEq("/Product/hazardousSubstances//substance", 2, document);
        assertEq("/Product/performance/performanceIndex/text()", 10, document);
        assertEq("/Product/performance/envPerformance/text()", 10.4d, document);
        assertEq("/Product/performance/recycled/text()", false, document);
        assertEq("/Product/productionDate/text()", "22/04/2024", document);
    }

    private void assertEq(String xpath, Object test, Node context) throws XPathExpressionException {
        XPath xpathI = XPathFactory.newInstance().newXPath();
        Object result = xpathI.compile(xpath).evaluate(context, XPathConstants.STRING);
        assertEquals(test.toString(), result);
    }

    private void assertLengthEq(String xpath, Integer expectedN, Node context)
            throws XPathExpressionException {
        Object result =
                XPathFactory.newInstance()
                        .newXPath()
                        .compile(xpath)
                        .evaluate(context, XPathConstants.NODESET);
        assertEquals(expectedN, ((NodeList) result).getLength());
    }
}
