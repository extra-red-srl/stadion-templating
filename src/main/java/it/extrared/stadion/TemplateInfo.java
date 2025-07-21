package it.extrared.stadion;

import it.extrared.stadion.formats.TemplateType;

public class TemplateInfo {

    private String templateName;

    private TemplateType templateType;

    private String _abstract;

    public TemplateInfo(String templateName, TemplateType templateType, String _abstract) {
        this.templateName = templateName;
        this.templateType = templateType;
        this._abstract = _abstract;
    }

    public TemplateInfo(String templateName, TemplateType templateType) {
        this.templateName = templateName;
        this.templateType = templateType;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public TemplateInfo setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
        return this;
    }

    public String get_abstract() {
        return _abstract;
    }

    public TemplateInfo set_abstract(String _abstract) {
        this._abstract = _abstract;
        return this;
    }

    public String getTemplateName() {
        return templateName;
    }

    public TemplateInfo setTemplateName(String templateName) {
        this.templateName = templateName;
        return this;
    }
}
