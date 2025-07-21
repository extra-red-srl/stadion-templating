package it.extrared.stadion.read;

import it.extrared.stadion.formats.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class JsonReaderFactory extends AbstractTemplateReaderFactory {

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES =
            Arrays.asList(MediaType.A_JSON_LD, MediaType.A_JSON, MediaType.T_JSON);

    public JsonReaderFactory() {
        super(SUPPORTED_MEDIA_TYPES);
    }

    @Override
    public TemplateReader createReader(InputStream inputStream) throws IOException {
        return new JsonTemplateReader(inputStream);
    }
}
