package it.extrared.stadion.write;

import it.extrared.stadion.formats.MediaType;
import java.util.List;

public abstract class AbstractOutputWriterFactory implements OutputWriterFactory {

    private List<MediaType> supportedMediaTypes;

    protected AbstractOutputWriterFactory(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    @Override
    public boolean supportsMediaType(MediaType mediaType) {
        return supportedMediaTypes.contains(mediaType);
    }

    @Override
    public int priority() {
        return 99;
    }
}
