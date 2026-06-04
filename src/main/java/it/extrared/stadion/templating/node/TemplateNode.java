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
