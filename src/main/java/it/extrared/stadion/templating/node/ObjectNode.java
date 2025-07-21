package it.extrared.stadion.templating.node;

import static it.extrared.stadion.utils.CommonUtils.tryIterator;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.write.OutputWriter;
import java.io.IOException;
import java.util.Iterator;

/** A {@link TemplateNode} representing a section of the template that represent a JSON Object. */
public class ObjectNode extends ContextNode {
    public ObjectNode(TemplateDirective key) {
        super(key);
    }

    @Override
    public void apply(Object object, OutputWriter writer) throws IOException {
        if (canWrite(object)) {
            String strKey = null;
            if (nodeName != null) {
                strKey = (String) nodeName.run(object);
                writer.writeFieldName(strKey, extraData);
            }
            writer.startElement(extraData);
            object = updateContext(object);
            for (TemplateNode node : getChildren()) {
                Iterator<?> iterator = tryIterator(object);
                if (iterator != null) {
                    while (iterator.hasNext()) {
                        node.apply(iterator.next(), writer);
                    }
                } else {
                    node.apply(object, writer);
                }
            }
            writer.endElement(extraData);
        }
    }
}
