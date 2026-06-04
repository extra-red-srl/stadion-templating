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

/**
 * Well-known structural node names recognised by Stadion template readers.
 *
 * <ul>
 *   <li>{@link #CTX} — context switch node ({@code $ctx} in JSON, {@code ctx} in XML)
 *   <li>{@link #IF} — conditional node ({@code $if} in JSON, {@code if} in XML)
 * </ul>
 */
public enum NodeName {
    /** Context switch node that changes the evaluation scope for its children. */
    CTX,
    /**
     * Conditional node that renders its children only when the filter evaluates to {@code true}.
     */
    IF;

    /**
     * Returns the lower-case XML element name for this node type.
     *
     * @return the XML name string
     */
    public String xmlValue() {
        return name().toLowerCase();
    }

    /**
     * Returns the JSON key representation (prefixed with {@code $}) for this node type.
     *
     * @return the JSON key string
     */
    public String jsonValue() {
        return "$" + name().toLowerCase();
    }
}
