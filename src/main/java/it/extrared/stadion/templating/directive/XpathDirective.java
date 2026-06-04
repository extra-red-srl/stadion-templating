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

import static org.w3c.dom.Node.ATTRIBUTE_NODE;

import it.extrared.stadion.utils.CommonUtils;
import it.extrared.stadion.utils.XmlUtils;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** Evaluates an XPath expression against the current DOM context node. */
public class XpathDirective extends FunctionDirective {

    private final String xpath;

    public XpathDirective(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public Object run(Object object) {
        if (!(object instanceof Node)) {
            return null;
        }
        try {
            NodeList nodeList =
                    (NodeList)
                            XPathFactory.newInstance()
                                    .newXPath()
                                    .compile(xpath)
                                    .evaluate(object, XPathConstants.NODESET);
            if (nodeList == null || nodeList.getLength() == 0) return null;
            if (nodeList.getLength() == 1) {
                Node node = nodeList.item(0);
                if (!node.hasChildNodes() || node.getNodeType() == ATTRIBUTE_NODE)
                    return XmlUtils.covertText(node.getTextContent());
                return node;
            } else return CommonUtils.toList(new XmlNodeIterator(nodeList));
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
