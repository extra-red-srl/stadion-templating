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
package it.extrared.stadion.catalog;

/**
 * Typed wrapper around the result of a {@link TemplateCatalog#saveTemplate} operation.
 *
 * <p>Use {@link #getResult(Class)} to obtain the underlying value cast to the expected type.
 */
public class SaveResult {

    private final Object result;

    private SaveResult(Object t) {
        this.result = t;
    }

    /**
     * Returns the raw result object.
     *
     * @return the save result; may be {@code null}
     */
    public Object getResult() {
        return result;
    }

    /**
     * Returns the result cast to the requested type.
     *
     * @param <T> the expected type
     * @param type the class to cast to
     * @return the typed result, or {@code null} if the result is {@code null}
     * @throws UnsupportedOperationException if the result cannot be cast to {@code type}
     */
    public <T> T getResult(Class<T> type) {
        if (result == null) return null;
        else if (!type.isAssignableFrom(result.getClass())) return type.cast(result);
        throw new UnsupportedOperationException(
                "Cannot convert %s to %s"
                        .formatted(result.getClass().getSimpleName(), type.getSimpleName()));
    }

    /**
     * Creates a new {@code SaveResult} wrapping the given object.
     *
     * @param obj the result to wrap
     * @return a new {@code SaveResult}
     */
    public static SaveResult of(Object obj) {
        return new SaveResult(obj);
    }
}
