package it.extrared.stadion.read;

import java.util.HashMap;
import java.util.Map;

public abstract class AbsractTemplateReader implements TemplateReader {

    protected Map<String, Object> globalProperties;

    protected AbsractTemplateReader() {
        this.globalProperties = new HashMap<>();
    }
}
