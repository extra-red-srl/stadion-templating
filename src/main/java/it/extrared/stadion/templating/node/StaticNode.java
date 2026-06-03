package it.extrared.stadion.templating.node;

import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.write.OutputWriter;
import java.io.IOException;

/**
 * A {@link TemplateNode} implementation representing a JSON field which is written to the output as
 * it is.
 */
public class StaticNode extends AbstractTemplateNode {

    private Object value;

    public StaticNode(TemplateDirective key, Object value) {
        super(key);
        this.value = value;
    }

    @Override
    public void apply(Object context, OutputWriter writer, NodeExecutionContext ctx)
            throws IOException {
        writer.writeFieldName((String) nodeName.run(context), extraData);
        writer.writeValue(value, extraData);
    }
}
