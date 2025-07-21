package it.extrared.stadion;

import it.extrared.stadion.formats.MediaType;

public interface MediaTypeHandlerService {

    int priority();

    boolean supportsMediaType(MediaType mediaType);
}
