package it.extrared.stadion.write;

import it.extrared.stadion.formats.MediaType;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JsonOutputWriterFactory extends AbstractOutputWriterFactory {

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES =
            Arrays.asList(MediaType.A_JSON, MediaType.T_JSON, MediaType.A_JSON_LD);

    public JsonOutputWriterFactory() {
        super(SUPPORTED_MEDIA_TYPES);
    }

    @Override
    public OutputWriter createOutputWriter(
            OutputStream outputStream, Map<String, Object> globalProperties) throws IOException {
        return new JsonOutputWriter(outputStream, globalProperties);
    }
}
