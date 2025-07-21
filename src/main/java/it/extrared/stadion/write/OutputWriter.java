package it.extrared.stadion.write;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

public interface OutputWriter extends Closeable {

    void startElement(Map<String, Object> extraData) throws IOException;

    void startCollection(Map<String, Object> extraData) throws IOException;

    void endElement(Map<String, Object> extraData) throws IOException;

    void endCollection(Map<String, Object> extraData) throws IOException;

    void writeFieldName(String fieldName, Map<String, Object> extraData) throws IOException;

    void writeValue(Object value, Map<String, Object> extraData) throws IOException;

    void close() throws IOException;
}
