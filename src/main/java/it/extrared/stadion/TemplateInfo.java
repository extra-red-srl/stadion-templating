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
package it.extrared.stadion;

import it.extrared.stadion.formats.TemplateType;

/** Describes a template by name, format, and optional abstract description. */
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
