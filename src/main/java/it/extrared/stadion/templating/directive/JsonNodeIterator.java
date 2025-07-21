package it.extrared.stadion.templating.directive;

import static it.extrared.stadion.templating.directive.JsonPointerDirective.toValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import java.util.Iterator;
import java.util.function.Consumer;

public class JsonNodeIterator implements Iterator<Object> {

    private int size;
    private Iterator<JsonNode> delegate;

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
