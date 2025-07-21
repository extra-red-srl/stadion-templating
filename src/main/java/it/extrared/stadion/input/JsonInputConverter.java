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
