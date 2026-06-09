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

import it.extrared.stadion.exceptions.UnsupportedInputTypeException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/** {@link TemplateInputConverter} for XML and XHTML input streams. */
public class XmlInputConverter extends AbstractInputConverter {

    private static final List<InputType> SUPPORTED_INPUT_TYPES = List.of(XML);

    private final DocumentBuilder documentBuilder;

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
        if (!(input instanceof InputStream))
            throw new UnsupportedInputTypeException(
                    "Input of type %s must be an %s"
                            .formatted(InputType.JSON, InputStream.class.getSimpleName()));
        try {
            return documentBuilder.parse((InputStream) input);
        } catch (SAXException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int priority() {
        return 99;
    }
}
