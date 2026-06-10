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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.extrared.stadion.exceptions.UnsupportedInputTypeException;
import it.extrared.stadion.log.LogWriter;
import it.extrared.stadion.log.LogWriters;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/** {@link TemplateInputConverter} for JSON input; deserialises content using Jackson. */
public class JsonInputConverter extends AbstractInputConverter {

    private static final List<InputType> INPUT_TYPES = List.of(InputType.JSON);

    private final ObjectMapper objectMapper;

    private static final LogWriter LOG = LogWriters.getLogger(JsonInputConverter.class);

    public JsonInputConverter() {
        super(INPUT_TYPES);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Object convert(Object input) throws UnsupportedInputTypeException, IOException {
        switch (input) {
            case null -> {
                return null;
            }
            case InputStream is -> {
                return objectMapper.readTree(is);
            }
            case JsonNode node -> {
                return node;
            }
            default -> {
                try {
                    return objectMapper.convertValue(input, JsonNode.class);
                } catch (IllegalArgumentException e) {
                    LOG.error("Error while converting input to JSON", e);
                    throw new UnsupportedInputTypeException(
                            "Cannot convert type: " + input.getClass().getName());
                }
            }
        }
    }

    @Override
    public int priority() {
        return 99;
    }
}
