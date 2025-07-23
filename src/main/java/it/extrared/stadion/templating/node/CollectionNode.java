package it.extrared.stadion.templating.node;

import static it.extrared.stadion.utils.CommonUtils.tryCollection;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.parser.InlineParser;
import it.extrared.stadion.write.OutputWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/** A {@link TemplateNode} representing a section of the template that represent a JSON Array. */
public class CollectionNode extends ContextNode {
    public CollectionNode(TemplateDirective key) {
        super(key);
    }

    @Override
    public void apply(Object object, OutputWriter writer) throws IOException {
        Collection<?> collection = tryCollection(object);
        if (collection != null) {
            for (Object val : collection) {
                evaluateSingle(val, writer);
            }
        } else {
            evaluateSingle(object, writer);
        }
    }

    private void evaluateSingle(Object object, OutputWriter writer) throws IOException {
        if (canWrite(object)) {
            boolean inline = false;
            if (nodeName != null) {
                String keyStr = (String) nodeName.run(object);
                inline = Objects.equals(InlineParser.INLINE, keyStr);
                if (!inline) writer.writeFieldName(keyStr, extraData);
            }
            if (!inline) writer.startCollection(extraData);
            object = updateContext(object);
            for (TemplateNode node : getChildren()) {
                node.addExtraData(PARENT_INLINE, inline);
                Collection<?> coll = tryCollection(object);
                if (coll != null) {
                    for (Object val : coll) {
                        node.apply(val, writer);
                    }
                } else {
                    node.apply(object, writer);
                }
            }
            if (!inline) writer.endCollection(extraData);
        }
    }
}
