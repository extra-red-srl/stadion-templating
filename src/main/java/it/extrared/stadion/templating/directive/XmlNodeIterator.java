package it.extrared.stadion.templating.directive;

import it.extrared.stadion.utils.XmlUtils;
import java.util.Iterator;
import java.util.function.Consumer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlNodeIterator implements Iterator<Object> {

    private NodeList nodeList;
    private int size;
    private int currentIndex;

    public XmlNodeIterator(NodeList nodeList) {
        this.nodeList = nodeList;
        this.currentIndex = 0;
        this.size = nodeList.getLength();
    }

    @Override
    public boolean hasNext() {
        return currentIndex < size;
    }

    @Override
    public Object next() {
        Node node = nodeList.item(currentIndex++);
        if (!node.hasChildNodes()) {
            return XmlUtils.covertText(node.getTextContent());
        }
        return node;
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
