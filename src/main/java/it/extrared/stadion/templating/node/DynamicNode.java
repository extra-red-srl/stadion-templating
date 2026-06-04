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
