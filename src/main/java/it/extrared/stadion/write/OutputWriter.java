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
package it.extrared.stadion.write;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * Abstraction over a streaming output format (JSON, XML, …).
 *
 * <p>
 * Template nodes call the methods on this interface to emit the rendered output
 * without being
 * coupled to a specific serialisation format. Implementations are responsible
 * for writing the
 * appropriate tokens ({@code {}, [], XML elements, etc.).
 */
public interface OutputWriter extends Closeable {

    /**
     * Opens an object (JSON {@code {}} or XML element start tag).
     *
     * @param extraData additional rendering hints from the template node
     * @throws IOException on write errors
     */
    void startElement(Map<String, Object> extraData) throws IOException;

    /**
     * Opens a collection (JSON {@code [}} or XML repeating element).
     *
     * @param extraData additional rendering hints from the template node
     * @throws IOException on write errors
     */
    void startCollection(Map<String, Object> extraData) throws IOException;

    /**
     * Closes the current object.
     *
     * @param extraData additional rendering hints from the template node
     * @throws IOException on write errors
     */
    void endElement(Map<String, Object> extraData) throws IOException;

    /**
     * Closes the current collection.
     *
     * @param extraData additional rendering hints from the template node
     * @throws IOException on write errors
     */
    void endCollection(Map<String, Object> extraData) throws IOException;

    /**
     * Writes a field name (JSON object key or XML element name).
     *
     * @param fieldName the name to write
     * @param extraData additional rendering hints from the template node
     * @throws IOException on write errors
     */
    void writeFieldName(String fieldName, Map<String, Object> extraData) throws IOException;

    /**
     * Writes a scalar value.
     *
     * @param value the value to serialise; may be {@code null}, a {@link String}, a {@link Number},
     *     a {@link Boolean}, or a date/time type
     * @param extraData additional rendering hints from the template node
     * @throws IOException on write errors
     */
    void writeValue(Object value, Map<String, Object> extraData) throws IOException;

    /** Flushes and closes the underlying stream. */
    void close() throws IOException;
}
