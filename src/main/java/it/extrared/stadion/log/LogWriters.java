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

import java.util.ServiceLoader;

/**
 * Factory accessor for {@link LogWriter} instances.
 *
 * <p>Uses {@link java.util.ServiceLoader} to discover a {@link LogWriterFactory}; falls back to the
 * built-in JUL-based {@link LogWriterFactoryImpl} when no SPI provider is registered.
 */
public class LogWriters {

    /**
     * Returns a {@link LogWriter} scoped to the given class.
     *
     * @param clazz the class whose name is used as the logger name
     * @return a ready-to-use logger
     */
    public static LogWriter getLogger(Class<?> clazz) {
        ServiceLoader<LogWriterFactory> loader = ServiceLoader.load(LogWriterFactory.class);
        return loader.findFirst().orElse(new LogWriterFactoryImpl()).getLogger(clazz);
    }
}
