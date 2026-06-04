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
package it.extrared.stadion.exceptions;

/**
 * Thrown when a requested template cannot be located in a {@link
 * it.extrared.stadion.catalog.TemplateCatalog}.
 */
public class TemplateNotFoundException extends Exception {

    /**
     * Creates a new exception with the given message.
     *
     * @param message identifies the missing template
     */
    public TemplateNotFoundException(String message) {
        super(message);
    }
}
