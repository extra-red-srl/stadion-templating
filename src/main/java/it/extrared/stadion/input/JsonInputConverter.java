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

import static it.extrared.stadion.formats.MediaType.A_JSON;
import static it.extrared.stadion.formats.MediaType.A_JSON_LD;
import static it.extrared.stadion.formats.MediaType.T_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.extrared.stadion.formats.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/** {@link TemplateInputConverter} for JSON input; deserialises content using Jackson. */
public class JsonInputConverter extends AbstractInputConverter {

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES =
            Arrays.asList(A_JSON, T_JSON, A_JSON_LD);

    private ObjectMapper objectMapper;

    public JsonInputConverter() {
        super(SUPPORTED_MEDIA_TYPES);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Object convert(InputStream inputStream) throws IOException {
        return objectMapper.readTree(inputStream);
    }

    @Override
    public int priority() {
        return 99;
    }
}
