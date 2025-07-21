package it.extrared.stadion.input;

import it.extrared.stadion.formats.MediaType;
import java.util.List;

public abstract class AbstractInputConverter implements TemplateInputConverter {

    private List<MediaType> supportedMediaTypes;

    protected AbstractInputConverter(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    @Override
    public boolean supportsMediaType(MediaType mediaType) {
        return supportedMediaTypes.contains(mediaType);
    }
}
