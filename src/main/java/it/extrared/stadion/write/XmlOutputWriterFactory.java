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
package it.extrared.stadion.write;

import it.extrared.stadion.formats.MediaType;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** {@link OutputWriterFactory} that creates {@link XmlOutputWriter} instances. */
public class XmlOutputWriterFactory extends AbstractOutputWriterFactory {

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES =
            Arrays.asList(MediaType.A_XML, MediaType.T_XML);

    public XmlOutputWriterFactory() {
        super(SUPPORTED_MEDIA_TYPES);
    }

    @Override
    public OutputWriter createOutputWriter(
            OutputStream outputStream, Map<String, Object> globalProperties) throws IOException {
        return new XmlOutputWriter(outputStream, globalProperties);
    }
}
