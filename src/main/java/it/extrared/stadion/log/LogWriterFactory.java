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
package it.extrared.stadion.log;

/**
 * SPI factory for {@link LogWriter} instances.
 *
 * <p>An alternative implementation can be provided by registering it via {@link
 * java.util.ServiceLoader}.
 */
public interface LogWriterFactory {

    /**
     * Returns a {@link LogWriter} for the given class.
     *
     * @param clazz the class requesting the logger; used as the logger name
     * @return a logger instance
     */
    LogWriter getLogger(Class<?> clazz);
}
