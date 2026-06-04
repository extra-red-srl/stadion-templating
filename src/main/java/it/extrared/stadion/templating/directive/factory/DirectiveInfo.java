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

/** This class represents the directive metadata. */
public class DirectiveInfo {

    private String name;

    private ParameterInfo[] parameters;

    public DirectiveInfo(String name, ParameterInfo... parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    /**
     * @return the directive name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the parameters' metadata.
     */
    public ParameterInfo[] getParameters() {
        return parameters;
    }
}
