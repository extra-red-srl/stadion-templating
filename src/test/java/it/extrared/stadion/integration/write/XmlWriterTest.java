package it.extrared.stadion.integration.write;

import static it.extrared.stadion.read.XmlTemplateReader.NAMESPACES;
import static it.extrared.stadion.read.XmlTemplateReader.XML_ATTRIBUTE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import it.extrared.stadion.write.XmlOutputWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class XmlWriterTest {

    @Test
    public void testWriteAttribute() throws IOException {
        Map<String, Object> map = Collections.emptyMap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XmlOutputWriter xmlOutputWriter =
                new XmlOutputWriter(baos, Map.of(NAMESPACES, Map.of("ns1", "http://example")));
        xmlOutputWriter.writeFieldName("example", map);
        xmlOutputWriter.startElement(map);
        xmlOutputWriter.writeFieldName("attribute", map);
        xmlOutputWriter.writeValue("value", Map.of(XML_ATTRIBUTE, true));
        xmlOutputWriter.writeFieldName("attribute2", map);
        xmlOutputWriter.writeValue("value2", Map.of(XML_ATTRIBUTE, true));
        xmlOutputWriter.endElement(map);
        xmlOutputWriter.close();
        String xml = baos.toString(StandardCharsets.UTF_8);
        assertEquals(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                        + "<example attribute=\"value\" attribute2=\"value2\"></example>",
                xml);
    }
}
