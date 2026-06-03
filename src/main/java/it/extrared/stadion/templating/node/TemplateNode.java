package it.extrared.stadion.templating.node;

import it.extrared.stadion.write.OutputWriter;
import java.io.IOException;
import java.util.List;

/**
 * Basic interface representing a template tree node obtained from the parsing of a template file. A
 * template node is responsible of writing the piece of output it represents.
 */
public interface TemplateNode {

    /**
     * Evaluate the node against the context (object) and write the result.
     *
     * @param object the context of the evaluation (the pojos that are being serialized).
     * @param writer the json writer.
     * @param ctx immutable runtime context propagated down the node tree.
     * @throws IOException
     */
    void apply(Object object, OutputWriter writer, NodeExecutionContext ctx) throws IOException;

    /** Convenience overload that starts execution with a root {@link NodeExecutionContext}. */
    default void apply(Object object, OutputWriter writer) throws IOException {
        apply(object, writer, NodeExecutionContext.ROOT);
    }

    /**
     * @return the children of this node if present.
     */
    List<TemplateNode> getChildren();

    /**
     * Adds a child to this node.
     *
     * @param templateNode the template node to add as a child.
     */
    void addChild(TemplateNode templateNode);

    /**
     * Return true if the key/field name of this template node is dynamic.
     *
     * @return true if dynamic, false otherwise.
     */
    boolean hasDynamicKey();

    void addExtraData(String key, Object value);
}
