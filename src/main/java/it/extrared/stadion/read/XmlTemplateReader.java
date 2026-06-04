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
package it.extrared.stadion.read;

import static it.extrared.stadion.templating.node.NodeName.CTX;
import static it.extrared.stadion.templating.node.NodeName.IF;
import static it.extrared.stadion.utils.CommonUtils.hasText;

import it.extrared.stadion.templating.directive.Literal;
import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.parser.DirectiveParser;
import it.extrared.stadion.templating.node.AbstractTemplateNode;
import it.extrared.stadion.templating.node.CollectionNode;
import it.extrared.stadion.templating.node.ContextNode;
import it.extrared.stadion.templating.node.DynamicNode;
import it.extrared.stadion.templating.node.NodeName;
import it.extrared.stadion.templating.node.ObjectNode;
import it.extrared.stadion.templating.node.StadionTemplate;
import it.extrared.stadion.templating.node.StaticNode;
import it.extrared.stadion.templating.node.TemplateNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/** {@link TemplateReader} that parses XML template files using the StAX streaming API. */
public class XmlTemplateReader extends AbsractTemplateReader {

    public static final String NAMESPACES = "namespaces";
    public static final String XML_ATTRIBUTE = "xmlAttribute";
    private static final String XML_COLLECTION = "collection";
    private static final String STADION_PREFIX = "stadion";
    private Map<String, String> namespaces;
    private Stack<StartElement> events;
    private XMLEventReader reader;

    private DirectiveParser directiveParser;

    private TemplateNode result;

    public XmlTemplateReader(InputStream inputStream) throws IOException {
        super();
        this.events = new Stack<>();
        this.namespaces = new HashMap<>();
        this.directiveParser = new DirectiveParser();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        try {
            reader = inputFactory.createXMLEventReader(inputStream);
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    @Override
    public StadionTemplate readTemplate() throws IOException {
        try {
            iterateEvents(null);
            Map<String, Object> globalProperties = new HashMap<>();
            globalProperties.put(NAMESPACES, namespaces);
            return new StadionTemplate(result, globalProperties);
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }

    private void iterateEvents(TemplateNode currentParent) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            TemplateNode actualParent = currentParent != null ? currentParent : result;
            if (event.isStartElement()) {
                handleStartElement(actualParent, event.asStartElement());
            } else if (event.isCharacters()) {
                handleCharacters(actualParent, event.asCharacters());
            } else if (event.isEndElement()) {
                // checks for either element with no children and no content
                // or the first start element
                if (!events.isEmpty()) handleEndElement(actualParent);
                break;
            }
        }
    }

    private void handleStartElement(TemplateNode currentParent, StartElement event)
            throws XMLStreamException {
        TemplateNode templateNode = null;
        XMLEvent startElement = null;
        parseNamespaces(event.getNamespaces());
        if (!events.isEmpty()) {
            startElement = events.pop();
            events.add(event);
            templateNode = createTemplateNode(currentParent, startElement.asStartElement());
        } else if (result == null) {
            startElement = event;
            templateNode = createTemplateNode(currentParent, startElement.asStartElement());
        }
        if (templateNode != null) iterateEvents(templateNode);
        else events.add(event);
    }

    private void handleCharacters(TemplateNode currentParent, Characters event)
            throws XMLStreamException {
        if (!events.isEmpty() && !event.isWhiteSpace()) {
            createTemplateNode(currentParent, event);
            iterateEvents(currentParent);
        }
    }

    private TemplateNode createTemplateNode(TemplateNode currentParent, StartElement event) {
        TemplateNode templateNode;
        String key = getKey(event.getName());
        TemplateDirective keyDir = directiveParser.parse(key);
        if (isCollection(event)) {
            templateNode = new CollectionNode(keyDir);
        } else {
            templateNode = new ObjectNode(keyDir);
        }
        TemplateDirective scope = getReserved(event, CTX);
        if (scope != null) ((ContextNode) templateNode).setCtxProperty(scope);

        TemplateDirective filter = getReserved(event, IF);
        if (filter != null) ((AbstractTemplateNode) templateNode).setFilter(filter);

        Iterator<Attribute> attributes = event.getAttributes();
        nodesFromAttributes(attributes, templateNode);
        if (currentParent != null) currentParent.addChild(templateNode);
        if (result == null) result = templateNode;
        return templateNode;
    }

    private TemplateNode handleEndElement(TemplateNode currentParent) {
        StartElement event = events.pop();
        return createTemplateNode(currentParent, event);
    }

    private void parseNamespaces(Iterator<Namespace> namespaces) {
        if (namespaces != null) {
            while (namespaces.hasNext()) {
                Namespace ns = namespaces.next();
                this.namespaces.put(ns.getPrefix(), ns.getNamespaceURI());
            }
        }
    }

    private TemplateNode createTemplateNode(TemplateNode currentParent, Characters characters) {
        StartElement startElement = events.pop();
        String key = getKey(startElement.getName());
        TemplateDirective keyDir = directiveParser.parse(key);
        TemplateNode templateNode;
        if (characters != null) {
            TemplateDirective directive = directiveParser.parse(characters.getData());
            if (directive instanceof Literal) {
                templateNode = new StaticNode(keyDir, directive.run(null));
            } else {
                templateNode = new DynamicNode(keyDir, directive);
            }
        } else {
            templateNode = new ObjectNode(keyDir);
        }
        TemplateDirective filter = getReserved(startElement, IF);
        if (filter != null) ((AbstractTemplateNode) templateNode).setFilter(filter);

        nodesFromAttributes(startElement.getAttributes(), templateNode);
        if (currentParent != null) currentParent.addChild(templateNode);
        else currentParent = templateNode;
        return currentParent;
    }

    private TemplateDirective getReserved(StartElement element, NodeName nodeName) {
        String uri = namespaces.get(STADION_PREFIX);
        Attribute attribute =
                element.getAttributeByName(new QName(uri, nodeName.xmlValue(), STADION_PREFIX));
        if (attribute != null && isStadionNsPrefix(attribute)) {
            return directiveParser.parse(attribute.getValue());
        }
        return null;
    }

    private void nodesFromAttributes(Iterator<Attribute> iterator, TemplateNode parent) {
        if (iterator != null) {
            while (iterator.hasNext()) nodeFromAttribute(parent, iterator.next());
        }
    }

    private void nodeFromAttribute(TemplateNode parent, Attribute attribute) {
        String localPart = attribute.getName().getLocalPart();
        if (attribute.isNamespace()
                || localPart.equals(XML_COLLECTION)
                || localPart.equals(IF.xmlValue())
                || localPart.equals(CTX.xmlValue())) return;
        String key = getKey(attribute.getName());
        TemplateDirective keyDir = directiveParser.parse(key);
        String value = attribute.getValue();
        TemplateDirective valueDir = directiveParser.parse(value);
        TemplateNode templateNode;
        if (valueDir instanceof Literal) templateNode = new StaticNode(keyDir, valueDir.run(null));
        else templateNode = new DynamicNode(keyDir, valueDir);
        templateNode.addExtraData(XML_ATTRIBUTE, true);
        parent.addChild(templateNode);
    }

    private String getKey(QName qName) {
        StringBuilder name = new StringBuilder();
        if (hasText(qName.getPrefix())) name.append(qName.getPrefix());
        name.append(qName.getLocalPart());
        return name.toString();
    }

    private boolean isCollection(StartElement element) {
        Attribute attribute = element.getAttributeByName(new QName(XML_COLLECTION));
        if (attribute != null && isStadionNsPrefix(attribute)) {
            String value = attribute.getValue();
            return Boolean.parseBoolean(value);
        }
        return false;
    }

    private boolean isStadionNsPrefix(Attribute attribute) {
        String nsPrefix = attribute.getName() != null ? attribute.getName().getPrefix() : null;
        return nsPrefix != null && nsPrefix.equals(STADION_PREFIX);
    }

    @Override
    public void close() throws IOException {
        try {
            reader.close();
        } catch (XMLStreamException e) {
            throw new IOException(e);
        }
    }
}
