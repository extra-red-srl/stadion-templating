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
