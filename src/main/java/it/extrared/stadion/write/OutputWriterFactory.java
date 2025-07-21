package it.extrared.stadion.write;

import it.extrared.stadion.MediaTypeHandlerService;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public interface OutputWriterFactory extends MediaTypeHandlerService {

    OutputWriter createOutputWriter(OutputStream outputStream, Map<String, Object> globalProperties)
            throws IOException;
}
