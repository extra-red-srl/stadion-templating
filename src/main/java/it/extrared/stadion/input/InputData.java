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
 * template execution.
 *
 * <p>Use the fluent {@link Builder} to construct instances:
 *
 * <pre>{@code
 * InputData input = InputData.builder()
 *         .mediaType(MediaType.A_JSON)
 *         .input(jsonStream)
 *         .build();
 * }</pre>
 */
public class InputData {

    private MediaType mediaType;

    private InputStream input;

    private InputData(MediaType mediaType, InputStream input) {
        this.mediaType = mediaType;
        this.input = input;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public InputStream getInput() {
        return input;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private MediaType mediaType;

        private InputStream input;

        public Builder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder input(InputStream input) {
            this.input = input;
            return this;
        }

        public InputData build() {
            return new InputData(mediaType, input);
        }
    }
}
