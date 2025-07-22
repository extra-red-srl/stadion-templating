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

    protected static final String PARENT_INLINE = "parent_inline";

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

    public TemplateDirective getFilter() {
        return filter;
    }

    public AbstractTemplateNode setFilter(TemplateDirective filter) {
        this.filter = filter;
        return this;
    }

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
