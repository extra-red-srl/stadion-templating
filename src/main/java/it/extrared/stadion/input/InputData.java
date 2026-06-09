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

import it.extrared.stadion.formats.MediaType;
import java.io.InputStream;

/**
 * Pairs an {@link InputStream} with its {@link MediaType}, representing one input source for a
 * template execution. *
 */
public class InputData {

    private InputType inputType;

    private Object input;

    private InputData(InputType inputType, Object input) {
        this.input = input;
        this.inputType = inputType;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public static InputData xmlInputData(InputStream input) {
        return new InputData(InputType.XML, input);
    }

    public static InputData jsonInputData(InputStream input) {
        return new InputData(InputType.JSON, input);
    }

    public static InputData pojoInputData(Object input) {
        return new InputData(InputType.POJO, input);
    }
}
