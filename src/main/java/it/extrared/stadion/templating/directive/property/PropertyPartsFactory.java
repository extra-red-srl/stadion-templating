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
