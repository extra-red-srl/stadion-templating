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

import java.util.LinkedList;
import java.util.regex.Pattern;

/** A factory for the various part of a property name eg. path.to.my.property. */
public class PropertyPartsFactory {

    private static final Pattern INDEX_PATTERN = Pattern.compile("^\\[\\d*\\]");

    private static final Pattern PROPERTY_PATTERN = Pattern.compile("^[^\\n ]*");

    public static LinkedList<PropertyPart> asParts(String propertyParts) {
        String[] parts = propertyParts.split("\\.");
        LinkedList<PropertyPart> partList = new LinkedList<>();
        for (String part : parts) {
            if (INDEX_PATTERN.matcher(part).matches()) partList.add(getIndex(part));
            else if (PROPERTY_PATTERN.matcher(part).matches()) partList.add(getPropertyName(part));
        }
        return partList;
    }

    private static PropertyName getPropertyName(String part) {
        return new PropertyName(part);
    }

    private static CollectionIndex getIndex(String part) {
        String digit = part.replace("[", "").replace("]", "");
        Integer index = Integer.valueOf(digit);
        return new CollectionIndex(index);
    }
}
