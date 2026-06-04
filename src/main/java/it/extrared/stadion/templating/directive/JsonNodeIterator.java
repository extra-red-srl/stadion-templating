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

import static it.extrared.stadion.templating.directive.JsonPointerDirective.toValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * {@link java.util.Iterator} over the values of a Jackson {@link
 * com.fasterxml.jackson.databind.JsonNode}.
 */
/**
 * {@link java.util.Iterator} over the values of a Jackson {@link
 * com.fasterxml.jackson.databind.JsonNode}.
 */
public class JsonNodeIterator implements Iterator<Object> {

    private final Iterator<JsonNode> delegate;

    public JsonNodeIterator(Iterator<JsonNode> iterator) {
        this.delegate = iterator;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public Object next() {
        JsonNode next = delegate.next();
        Object result = null;
        if (next != null)
            if (next.isValueNode()) result = toValue((ValueNode) next);
            else result = next;
        return result;
    }

    @Override
    public void remove() {
        Iterator.super.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super Object> action) {
        Iterator.super.forEachRemaining(action);
    }
}
