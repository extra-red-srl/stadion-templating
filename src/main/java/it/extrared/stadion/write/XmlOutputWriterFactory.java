package it.extrared.stadion.write;

import it.extrared.stadion.formats.MediaType;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
