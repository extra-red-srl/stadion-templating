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
