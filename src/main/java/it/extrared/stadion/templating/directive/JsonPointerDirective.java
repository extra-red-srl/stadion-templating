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
package it.extrared.stadion.templating.directive;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import it.extrared.stadion.utils.CommonUtils;
import java.io.IOException;

/** Navigates a Jackson JSON tree using a JSON Pointer expression (RFC 6901). */
public class JsonPointerDirective extends FunctionDirective {

    private final JsonPointer jsonPointer;

    public JsonPointerDirective(String pointer) {
        this.jsonPointer = JsonPointer.compile(pointer);
    }

    @Override
    public Object run(Object object) {
        if (object instanceof JsonNode) {
            JsonNode node = ((JsonNode) object).at(jsonPointer);
            Object result;
            if (node.isMissingNode() || node.isNull()) result = null;
            else if (node.isValueNode()) result = toValue((ValueNode) node);
            else if (node.isArray())
                result = CommonUtils.toList(new JsonNodeIterator(node.iterator()));
            else result = node;
            return result;
        } else {
            return null;
        }
    }

    static Object toValue(ValueNode valueNode) {
        Object result = null;
        if (valueNode.isDouble()) result = valueNode.doubleValue();
        else if (valueNode.isTextual()) result = valueNode.textValue();
        else if (valueNode.isInt()) result = valueNode.intValue();
        else if (valueNode.isLong()) result = valueNode.longValue();
        else if (valueNode.isFloat()) result = valueNode.floatValue();
        else if (valueNode.isBoolean()) result = valueNode.booleanValue();
        else if (valueNode.isBinary()) {
            try {
                result = valueNode.binaryValue();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
