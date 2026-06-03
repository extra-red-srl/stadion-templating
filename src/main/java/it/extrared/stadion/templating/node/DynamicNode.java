package it.extrared.stadion.templating.node;

import it.extrared.stadion.templating.directive.FilterAndDirective;
import it.extrared.stadion.templating.directive.TemplateDirective;
import it.extrared.stadion.write.OutputWriter;
import java.io.IOException;

/**
 * A {@link TemplateNode} implementation representing a JSON field which is obtained through the
 * evaluation of a template directive against the context.
 */
public class DynamicNode extends AbstractTemplateNode {

    private TemplateDirective directive;

    public DynamicNode(TemplateDirective key, TemplateDirective directive) {
        super(key);
        if (directive instanceof FilterAndDirective) {
            FilterAndDirective filterAndDirective = (FilterAndDirective) directive;
            this.directive = filterAndDirective.getDirective();
            this.filter = filterAndDirective.getFilter();
        } else {
            this.directive = directive;
        }
    }

    @Override
    public void apply(Object context, OutputWriter writer, NodeExecutionContext ctx)
            throws IOException {
        if (canWrite(context)) {
            Object value = directive.run(context);
            writer.writeFieldName((String) nodeName.run(context), extraData);
            writer.writeValue(value, extraData);
        }
    }
}
