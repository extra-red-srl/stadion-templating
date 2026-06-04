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
package it.extrared.stadion.templating.directive.factory;

/** Metadata of a parameter of a function. */
public class ParameterInfo {

    private Class<?> type;

    private Integer pos;
    private boolean optional;

    public ParameterInfo(Class<?> type, Integer pos) {
        this(type, pos, false);
    }

    public ParameterInfo(Class<?> type, Integer pos, boolean optional) {
        this.type = type;
        this.pos = pos;
        this.optional = optional;
    }

    /**
     * @return the type of the parameter.
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Set the type of the parameter.
     *
     * @param type the param type
     * @return this object.
     */
    public ParameterInfo setType(Class<?> type) {
        this.type = type;
        return this;
    }

    /**
     * Returns the 0-based position of this parameter in the directive's argument list.
     *
     * @return the parameter position
     */
    public Integer getPos() {
        return pos;
    }

    /**
     * Sets the position of this parameter.
     *
     * @param pos the 0-based position
     * @return this object
     */
    public ParameterInfo setPos(Integer pos) {
        this.pos = pos;
        return this;
    }

    public boolean isOptional() {
        return optional;
    }
}
