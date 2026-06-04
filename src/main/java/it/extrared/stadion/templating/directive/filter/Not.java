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
package it.extrared.stadion.templating.directive.filter;

/** A Filter that negates the result of the wrapped filter. */
public class Not implements Filter {

    private final Filter arg;

    public Not(Filter arg) {
        this.arg = arg;
    }

    @Override
    public boolean evaluate(Object context) {
        return !arg.evaluate(context);
    }
}
