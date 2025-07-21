package it.extrared.stadion.read;

import it.extrared.stadion.MediaTypeHandlerService;
import java.io.IOException;
import java.io.InputStream;

public interface TemplateReaderFactory extends MediaTypeHandlerService {

    TemplateReader createReader(InputStream inputStream) throws IOException;
}
