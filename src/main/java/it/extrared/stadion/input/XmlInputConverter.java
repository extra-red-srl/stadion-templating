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
package it.extrared.stadion.input;

import static it.extrared.stadion.input.InputType.XML;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.extrared.stadion.exceptions.UnsupportedInputTypeException;
import it.extrared.stadion.log.LogWriter;
import it.extrared.stadion.log.LogWriters;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/** {@link TemplateInputConverter} for XML and XHTML input streams. */
public class XmlInputConverter extends AbstractInputConverter {

    private static final List<InputType> SUPPORTED_INPUT_TYPES = List.of(XML);

    private final DocumentBuilder documentBuilder;

    private static final LogWriter LOG = LogWriters.getLogger(XmlInputConverter.class);

    public XmlInputConverter() {
        super(SUPPORTED_INPUT_TYPES);
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        try {
            this.documentBuilder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object convert(Object input) throws IOException, UnsupportedInputTypeException {
        try {
            switch (input) {
                case null -> {
                    return null;
                }
                case InputStream is -> {
                    return documentBuilder.parse(is);
                }
                case Document doc -> {
                    return doc;
                }
                default -> {
                    return convertPojo(input);
                }
            }

        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new IOException(e);
        }
    }

    private Document convertPojo(Object input)
            throws IOException, ParserConfigurationException, SAXException {
        XmlMapper xmlMapper = new XmlMapper();
        byte[] bytes = xmlMapper.writeValueAsBytes(input);
        return documentBuilder.parse(new ByteArrayInputStream(bytes));
    }

    @Override
    public int priority() {
        return 99;
    }
}
