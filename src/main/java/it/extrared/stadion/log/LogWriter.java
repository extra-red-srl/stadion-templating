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

import java.util.function.Supplier;

/**
 * Logging abstraction used throughout the library to avoid a hard dependency on a specific logging
 * framework. The default implementation delegates to {@link java.util.logging.Logger}.
 *
 * <p>Every logging method has two overloads: a plain {@link String} overload and a {@link
 * java.util.function.Supplier Supplier&lt;String&gt;} overload. Prefer the supplier overload in hot
 * paths to avoid string formatting when the log level is disabled.
 */
public interface LogWriter {

    void info(String message);

    void info(String message, Exception e);

    void info(Supplier<String> message);

    void info(Supplier<String> message, Exception e);

    void debug(String message);

    void debug(String message, Exception e);

    void debug(Supplier<String> message);

    void debug(Supplier<String> message, Exception e);

    void warn(String message);

    void warn(String message, Exception e);

    void warn(Supplier<String> message);

    void warn(Supplier<String> message, Exception e);

    void error(String message);

    void error(String message, Exception e);

    void error(String message, Throwable e);

    void error(Supplier<String> message);

    void error(Supplier<String> message, Exception e);

    void error(Supplier<String> message, Throwable e);

    void trace(String message);

    void trace(String message, Exception e);

    void trace(Supplier<String> message);

    void trace(Supplier<String> message, Exception e);
}
