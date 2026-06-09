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
package it.extrared.stadion.input;

import it.extrared.stadion.exceptions.UnsupportedInputTypeException;
import java.io.IOException;
import java.util.List;

public class PojoInputConverter extends AbstractInputConverter {
    public PojoInputConverter() {
        super(List.of(InputType.POJO));
    }

    @Override
    public Object convert(Object input) throws UnsupportedInputTypeException, IOException {
        return input;
    }

    @Override
    public int priority() {
        return 99;
    }
}
