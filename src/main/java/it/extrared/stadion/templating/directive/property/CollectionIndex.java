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
package it.extrared.stadion.templating.directive.property;

import it.extrared.stadion.exceptions.TemplatingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/** A property name part that represent an element in a collection. */
public class CollectionIndex implements PropertyPart {

    private Integer index;

    public CollectionIndex(Integer index) {
        this.index = index;
    }

    @Override
    public Object evaluate(Object o) {
        if (o == null) return null;
        List<?> collection;
        if (o instanceof List<?>) {
            collection = (List<?>) o;
        } else if (o instanceof Collection<?>) {
            collection = new ArrayList<>((Collection<?>) o);
        } else if (o.getClass().isArray()) {
            collection = Arrays.asList((Object[]) o);
        } else {
            throw new TemplatingException(
                    "Found property with index [%s] in path, but the received object wasn't an iterable..."
                            .formatted(index));
        }
        int size = collection.size();
        if (index >= size)
            throw new TemplatingException(
                    "Found property with index [%s] in path, but the received iterable has size %s..."
                            .formatted(index, size));
        return collection.get(index);
    }
}
