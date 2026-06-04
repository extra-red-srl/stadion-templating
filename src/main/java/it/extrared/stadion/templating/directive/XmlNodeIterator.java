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

import it.extrared.stadion.utils.XmlUtils;
import java.util.Iterator;
import java.util.function.Consumer;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** {@link java.util.Iterator} over the children of a DOM {@link org.w3c.dom.NodeList}. */
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
