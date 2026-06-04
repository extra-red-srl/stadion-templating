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
package it.extrared.stadion.templating.node;

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.templating.directive.Literal;
import it.extrared.stadion.templating.directive.TemplateDirective;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of a {@link TemplateNode} providing basic implementation for some
 * methods.
 */
public abstract class AbstractTemplateNode implements TemplateNode {

    protected LinkedList<TemplateNode> nodes;

    protected TemplateDirective nodeName;

    protected TemplateDirective filter;

    protected Map<String, Object> extraData;

    protected AbstractTemplateNode(TemplateDirective nodeName) {
        this.nodeName = nodeName;
        this.nodes = new LinkedList<>();
        this.extraData = new HashMap<>();
    }

    @Override
    public List<TemplateNode> getChildren() {
        return nodes;
    }

    @Override
    public void addChild(TemplateNode templateNode) {
        nodes.addLast(templateNode);
    }

    @Override
    public boolean hasDynamicKey() {
        return nodeName != null && !(nodeName instanceof Literal);
    }

    /**
     * Returns the optional filter directive used to decide at runtime whether this node should be
     * written.
     *
     * @return the filter, or {@code null} when no filter is set
     */
    public TemplateDirective getFilter() {
        return filter;
    }

    /**
     * Sets the filter directive that controls conditional output of this node.
     *
     * @param filter the filter to apply; {@code null} disables filtering
     * @return this node, for chaining
     */
    public AbstractTemplateNode setFilter(TemplateDirective filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Evaluates the optional filter against the current {@code context}.
     *
     * @param context the runtime evaluation context
     * @return {@code true} if the node should be written (no filter, or filter returns {@link
     *     Boolean#TRUE})
     * @throws it.extrared.stadion.exceptions.TemplatingException if the filter does not return a
     *     {@link Boolean}
     */
    public boolean canWrite(Object context) {
        if (filter == null) return true;
        Object result = filter.run(context);
        if (!(result instanceof Boolean))
            throw new TemplatingException("Filter did not return a boolean value");
        return ((Boolean) result).booleanValue();
    }

    @Override
    public void addExtraData(String key, Object value) {
        extraData.put(key, value);
    }
}
