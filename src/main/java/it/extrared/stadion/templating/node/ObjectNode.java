package it.extrared.stadion.templating.node;

import static it.extrared.stadion.utils.CommonUtils.tryCollection;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.templating.directive.parser.InlineParser;
import it.extrared.stadion.write.OutputWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

/** A {@link TemplateNode} representing a section of the template that represent a JSON Object. */
public class ObjectNode extends ContextNode {

    public ObjectNode(TemplateDirective key) {
        super(key);
    }

    @Override
    public void apply(Object object, OutputWriter writer, NodeExecutionContext ctx)
            throws IOException {
        if (canWrite(object)) {
            String strKey = null;
            boolean inline = ctx.parentInline();
            if (nodeName != null) {
                strKey = (String) nodeName.run(object);
                inline = Objects.equals(strKey, InlineParser.INLINE);
                if (!inline) writer.writeFieldName(strKey, extraData);
            }
            if (!inline) writer.startElement(extraData);
            object = updateContext(object);
            for (TemplateNode node : getChildren()) {
                Collection<?> coll = tryCollection(object);
                if (coll != null) {
                    for (Object o : coll) node.apply(o, writer, NodeExecutionContext.ROOT);
                } else {
                    node.apply(object, writer, NodeExecutionContext.ROOT);
                }
            }
            if (!inline) writer.endElement(extraData);
        }
    }
}
