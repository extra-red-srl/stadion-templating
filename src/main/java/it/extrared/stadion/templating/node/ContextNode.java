package it.extrared.stadion.templating.node;

import static it.extrared.stadion.utils.CommonUtils.tryIterator;

import it.extrared.stadion.log.LogWriter;
import it.extrared.stadion.log.LogWriters;
import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.write.OutputWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An abstract implementation of a {@link TemplateNode} representing a node able to set the
 * evauation context or scope for its children nodes (like {@link ObjectNode} and {@link
 * CollectionNode}), by evaluating a property name against the evaluation context.
 */
public abstract class ContextNode extends AbstractTemplateNode {

    private static final LogWriter LOG = LogWriters.getLogger(ContextNode.class);

    private TemplateDirective ctxProperty;

    ContextNode(TemplateDirective key) {
        super(key);
    }

    /**
     * Sets the scope property.
     *
     * @param ctxProperty the property to be evaluated to provide a new evaluation context.
     * @return this node.
     */
    public ContextNode setCtxProperty(TemplateDirective ctxProperty) {
        this.ctxProperty = ctxProperty;
        return this;
    }

    /**
     * Iterate the children and evaluate them.
     *
     * @param object the evaluation context.
     * @param writer the writer.
     * @throws IOException
     */
    protected void evalOnChildren(Object object, OutputWriter writer) throws IOException {
        for (TemplateNode node : getChildren()) {
            node.apply(object, writer);
        }
    }

    /**
     * Updates the evaluation current evaluation context.
     *
     * @param object the current context.
     * @return the new context.
     */
    protected Object updateContext(Object object) {
        if (ctxProperty == null) return object;
        Iterator<?> iterator = tryIterator(object);
        Object result = null;
        if (iterator != null) {
            List<Object> contexts = new ArrayList<>();
            while (iterator.hasNext()) {
                Object newCtx = ctxProperty.run(iterator.next());
                if (newCtx != null) contexts.add(newCtx);
            }
            if (contexts.size() == 1) result = contexts.get(0);
            else result = contexts;
        } else {
            result = ctxProperty.run(object);
        }
        return result;
    }

    /**
     * @return the scope property name.
     */
    public TemplateDirective getCtxProperty() {
        return ctxProperty;
    }
}
