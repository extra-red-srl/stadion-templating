package it.extrared.stadion.catalog;

import it.extrared.stadion.formats.TemplateType;

public class TemplateMetadata<ID> {

    private TemplateType templateType;

    private String name;

    private ID id;

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
