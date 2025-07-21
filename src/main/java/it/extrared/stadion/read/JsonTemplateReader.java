package it.extrared.stadion.read;

import static it.extrared.stadion.templating.node.NodeName.CTX;
import static it.extrared.stadion.templating.node.NodeName.IF;
import static it.extrared.stadion.utils.CommonUtils.hasText;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.parser.DirectiveParser;
import it.extrared.stadion.templating.node.AbstractTemplateNode;
import it.extrared.stadion.templating.node.CollectionNode;
import it.extrared.stadion.templating.node.ContextNode;
import it.extrared.stadion.templating.node.DynamicNode;
import it.extrared.stadion.templating.node.ObjectNode;
import it.extrared.stadion.templating.node.StadionTemplate;
import it.extrared.stadion.templating.node.StaticNode;
import it.extrared.stadion.templating.node.TemplateNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JsonTemplateReader extends AbsractTemplateReader {

    private DirectiveParser directiveParser;
    private Map<String, Object> extraData;
    private JsonParser jsonParser;

    private JsonParser jParser;

    public JsonTemplateReader(InputStream inputStream) throws IOException {
        super();
        this.directiveParser = new DirectiveParser();
        this.extraData = Collections.emptyMap();
        JsonFactory factory = new JsonFactory();
        factory.enable(JsonParser.Feature.ALLOW_COMMENTS);
        try {
            this.jParser = factory.createParser(inputStream);
        } catch (JsonParseException e) {
            throw new IOException(e);
        }
    }

    @Override
    public StadionTemplate readTemplate() throws IOException {
        JsonToken token = jParser.nextToken();
        TemplateNode templateNode = dispatchParse(jParser, token, null);
        return new StadionTemplate(templateNode, globalProperties);
    }

    @Override
    public void close() throws IOException {
        jParser.close();
    }

    private TemplateNode dispatchParse(
            JsonParser jParser, JsonToken token, TemplateNode currentParent) throws IOException {
        if (token == JsonToken.START_OBJECT) {
            return parseObject(jParser, jParser.currentName(), currentParent);
        } else if (token == JsonToken.START_ARRAY) {
            return parseArray(jParser, jParser.currentName(), currentParent);
        } else if (token != JsonToken.FIELD_NAME) {
            return parseValue(
                    retrieveValue(jParser, token), jParser.currentName(), token, currentParent);
        }
        return currentParent;
    }

    private TemplateNode parseObject(
            JsonParser jParser, String currentKey, TemplateNode currentParent) throws IOException {
        TemplateDirective key = null;
        if (hasText(currentKey)) key = directiveParser.parse(currentKey);
        ObjectNode objectNode = new ObjectNode(key);
        JsonToken token = jParser.nextToken();
        while (token != JsonToken.END_OBJECT) {
            dispatchParse(jParser, token, objectNode);
            token = jParser.nextToken();
        }
        if (isArrayDirectiveObject(objectNode, currentParent)) {
            if (objectNode.getCtxProperty() != null)
                ((CollectionNode) currentParent).setCtxProperty(objectNode.getCtxProperty());
            if (objectNode.getFilter() != null)
                ((AbstractTemplateNode) currentParent).setFilter(objectNode.getFilter());
        } else if (currentParent != null) currentParent.addChild(objectNode);

        return currentParent != null ? currentParent : objectNode;
    }

    private boolean isArrayDirectiveObject(ObjectNode objectNode, TemplateNode currentParent) {
        return objectNode.getChildren().isEmpty()
                && (objectNode.getCtxProperty() != null || objectNode.getFilter() != null)
                && currentParent instanceof CollectionNode;
    }

    private TemplateNode parseArray(
            JsonParser jParser, String currentKey, TemplateNode currentParent) throws IOException {
        TemplateDirective key = null;
        if (hasText(currentKey)) key = directiveParser.parse(currentKey);
        CollectionNode collectionNode = new CollectionNode(key);
        if (currentParent != null) currentParent.addChild(collectionNode);
        JsonToken token = jParser.nextToken();
        while (token != JsonToken.END_ARRAY) {
            dispatchParse(jParser, token, collectionNode);
            token = jParser.nextToken();
        }
        fixScopeForArray(collectionNode);
        return currentParent != null ? currentParent : collectionNode;
    }

    private void fixScopeForArray(CollectionNode collectionNode) {
        List<TemplateNode> children = collectionNode.getChildren();
        if (!children.isEmpty()) {
            TemplateNode first = children.get(0);
            if (first instanceof ObjectNode && first.getChildren().isEmpty()) {
                ContextNode contextNode = (ContextNode) first;
                collectionNode.setCtxProperty(contextNode.getCtxProperty());
                if (contextNode.getFilter() != null)
                    collectionNode.setFilter(contextNode.getFilter());
                collectionNode.getChildren().remove(first);
            }
        }
    }

    private TemplateNode parseValue(
            Object value, String currentKey, JsonToken token, TemplateNode currentParent)
            throws IOException {
        TemplateDirective key = null;
        if (hasText(currentKey) && !isParent(currentKey) && !isFilter(currentKey))
            key = directiveParser.parse(currentKey);
        if (value instanceof String) {
            String str = value.toString();
            TemplateDirective templateDirective = directiveParser.parse(str);
            if (isParent(currentKey) && currentParent instanceof ContextNode) {
                ((ContextNode) currentParent).setCtxProperty(templateDirective);
            } else if (isFilter(currentKey)) {
                ((AbstractTemplateNode) currentParent).setFilter(templateDirective);
            } else if (templateDirective != null) {
                DynamicNode dynamicNode = new DynamicNode(key, templateDirective);
                currentParent.addChild(dynamicNode);
            } else {
                currentParent.addChild(new StaticNode(key, value));
            }
        } else {
            currentParent.addChild(new StaticNode(key, value));
        }
        return currentParent;
    }

    private Object retrieveValue(JsonParser jParser, JsonToken token) throws IOException {
        Object result = null;
        if (token != null) {
            if (token.isBoolean()) result = jParser.getBooleanValue();
            else if (token.isNumeric()) result = jParser.getNumberValue();
            else if (token.isScalarValue()) result = jParser.getText();
        }
        return result;
    }

    private boolean isParent(String key) {
        return key.equals(CTX.jsonValue());
    }

    private boolean isFilter(String key) {
        return key.equals(IF.jsonValue());
    }
}
