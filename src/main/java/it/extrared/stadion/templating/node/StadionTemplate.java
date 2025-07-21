package it.extrared.stadion.templating.node;

import it.extrared.stadion.write.OutputWriter;
import java.io.IOException;
import java.util.Map;

public class StadionTemplate {

    private final Map<String, Object> globalProperties;
    private final TemplateNode templateNode;

    public StadionTemplate(TemplateNode templateNode, Map<String, Object> globalProperties) {
        this.globalProperties = globalProperties;
        this.templateNode = templateNode;
    }

    public Map<String, Object> getGlobalProperties() {
        return globalProperties;
    }

    public void exec(Object value, OutputWriter outputWriter) throws IOException {
        templateNode.apply(value, outputWriter);
    }
}
