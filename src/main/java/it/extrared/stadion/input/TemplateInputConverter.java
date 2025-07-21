package it.extrared.stadion.input;

import it.extrared.stadion.MediaTypeHandlerService;
import java.io.IOException;
import java.io.InputStream;

public interface TemplateInputConverter extends MediaTypeHandlerService {

    Object convert(InputStream inputStream) throws IOException;
}
