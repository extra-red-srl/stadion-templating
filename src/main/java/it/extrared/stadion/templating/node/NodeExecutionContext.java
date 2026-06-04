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
 * Immutable value object that carries runtime-only state through the template node execution tree.
 *
 * <p>Keeping this data separate from the node instances (which are shared and reused across every
 * {@code apply()} invocation) ensures that cached {@link StadionTemplate} objects are safe for
 * concurrent use: each call to {@link StadionTemplate#exec} allocates its own context objects on
 * the stack and never mutates shared state.
 */
public record NodeExecutionContext(boolean parentInline) {

    /** Root context used at the start of every template execution. */
    public static final NodeExecutionContext ROOT = new NodeExecutionContext(false);

    /** Returns a new context with the given {@code parentInline} flag. */
    public NodeExecutionContext withParentInline(boolean inline) {
        return inline == parentInline ? this : new NodeExecutionContext(inline);
    }
}
