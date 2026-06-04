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
package it.extrared.stadion.integration.write;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.extrared.stadion.write.JsonOutputWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class JsonOutputWriterTest {

    @Test
    public void testWriteStringArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(new String[] {"a", "b", "c"}, null);
        writer.close();
        assertEquals("[\"a\",\"b\",\"c\"]", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteIntArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(new int[] {1, 2, 3}, null);
        writer.close();
        assertEquals("[1,2,3]", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteDoubleArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(new double[] {1.1, 2.2, 3.3}, null);
        writer.close();
        assertEquals("[1.1,2.2,3.3]", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteBoolArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(new boolean[] {true, false, true}, null);
        writer.close();
        assertEquals("[true,false,true]", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteLongArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(new long[] {1L, 2L, 3L}, null);
        writer.close();
        assertEquals("[1,2,3]", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteShortArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(new short[] {1, 2, 3}, null);
        writer.close();
        assertEquals("[1,2,3]", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteCharArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(new char[] {'a', 'b', 'c'}, null);
        writer.close();
        assertEquals("[\"a\",\"b\",\"c\"]", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteDouble() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(2.4, null);
        writer.close();
        assertEquals("2.4", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteInteger() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(2, null);
        writer.close();
        assertEquals("2", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteLong() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(2L, null);
        writer.close();
        assertEquals("2", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteBool() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue(true, null);
        writer.close();
        assertEquals("true", baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void testWriteChar() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JsonOutputWriter writer = new JsonOutputWriter(baos, Collections.emptyMap());
        writer.writeValue('a', null);
        writer.close();
        assertEquals("\"a\"", baos.toString(StandardCharsets.UTF_8));
    }
}
