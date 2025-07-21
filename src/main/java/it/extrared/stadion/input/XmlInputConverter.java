package it.extrared.stadion.input;

import static it.extrared.stadion.formats.MediaType.A_XHTML;
import static it.extrared.stadion.formats.MediaType.A_XML;
import static it.extrared.stadion.formats.MediaType.T_XHTML;
import static it.extrared.stadion.formats.MediaType.T_XML;

import it.extrared.stadion.formats.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class XmlInputConverter extends AbstractInputConverter {

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES =
            Arrays.asList(A_XML, T_XML, A_XHTML, T_XHTML);

    private DocumentBuilder documentBuilder;

    public XmlInputConverter() {
        super(SUPPORTED_MEDIA_TYPES);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        try {
            this.documentBuilder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object convert(InputStream inputStream) throws IOException {
        try {
            return documentBuilder.parse(inputStream);
        } catch (SAXException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int priority() {
        return 99;
    }
}
