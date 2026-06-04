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
package it.extrared.stadion.write;

import static it.extrared.stadion.read.XmlTemplateReader.NAMESPACES;
import static it.extrared.stadion.read.XmlTemplateReader.XML_ATTRIBUTE;

import it.extrared.stadion.utils.CommonUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XmlOutputWriter implements OutputWriter {

    private final XMLStreamWriter writer;
    private String currentFieldName;
    private final Map<String, String> namespaces;

    public XmlOutputWriter(OutputStream outputStream, Map<String, Object> globalProperties)
            throws IOException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        try {
            this.writer = output.createXMLStreamWriter(outputStream);
            writer.writeStartDocument("utf-8", "1.0");
            Object nsMap = globalProperties.get(NAMESPACES);
            if (nsMap == null) namespaces = Collections.emptyMap();
            else namespaces = (Map<String, String>) nsMap;
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void startElement(Map<String, Object> extraData) throws IOException {
        String[] fieldName = currentFieldName.split(":");
        if (fieldName.length > 1) {
            String nsPrefix = fieldName[0];
            String nsUri = namespaces.get(nsPrefix);
            try {
                writer.writeStartElement(nsPrefix, fieldName[1], nsUri);
            } catch (XMLStreamException e) {
                throw new IOException(e);
            }
        } else {
            try {
                writer.writeStartElement(fieldName[0]);
            } catch (XMLStreamException e) {
                throw new IOException(e);
            }
        }
    }

    @Override
    public void startCollection(Map<String, Object> extraData) throws IOException {}

    @Override
    public void endElement(Map<String, Object> extraData) throws IOException {
        try {
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void endCollection(Map<String, Object> extraData) throws IOException {}

    @Override
    public void writeFieldName(String fieldName, Map<String, Object> extraData) throws IOException {
        this.currentFieldName = fieldName;
    }

    @Override
    public void writeValue(Object value, Map<String, Object> extraData) throws IOException {
        if (isXmlAttribute(extraData)) writeAttribute(value);
        else {
            Collection<?> collection = CommonUtils.tryCollection(value);
            if (collection != null)
                for (Object val : collection) writeValueInternal(val, extraData);
            else writeValueInternal(value, extraData);
        }
    }

    private void writeAttribute(Object value) throws IOException {
        String[] nameArray = currentFieldName.split(":");
        if (nameArray.length > 1) {
            String prefix = nameArray[0];
            String uri = namespaces.get(prefix);
            try {
                writer.writeAttribute(uri, nameArray[1], value.toString());
            } catch (XMLStreamException e) {
                throw new IOException(e);
            }
        } else {
            try {
                writer.writeAttribute(nameArray[0], value.toString());
            } catch (XMLStreamException e) {
                throw new IOException(e);
            }
        }
    }

    private boolean isXmlAttribute(Map<String, Object> extraData) {
        Object isAttr = extraData.get(XML_ATTRIBUTE);
        if (isAttr != null) return ((Boolean) isAttr).booleanValue();
        return false;
    }

    private void writeValueInternal(Object value, Map<String, Object> extraData)
            throws IOException {
        try {
            String chars = asString(value);
            if (chars != null) {
                startElement(extraData);
                writer.writeCharacters(chars);
                endElement(extraData);
            }
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            this.writer.close();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    private String asString(Object value) {
        if (value == null) return null;
        String result = null;
        if (value instanceof Date) {
            result = CommonUtils.defFormatDate((Date) value);
        } else if (value instanceof TemporalAccessor) {
            result = CommonUtils.defFormatTemporalAccessor((TemporalAccessor) value);
        } else {
            result = value.toString();
        }
        return result;
    }
}
