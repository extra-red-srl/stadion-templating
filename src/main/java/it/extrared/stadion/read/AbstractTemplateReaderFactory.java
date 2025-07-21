package it.extrared.stadion.read;

import it.extrared.stadion.formats.MediaType;
import java.util.List;

abstract class AbstractTemplateReaderFactory implements TemplateReaderFactory {

    private List<MediaType> supportedMediaTypes;

    public AbstractTemplateReaderFactory(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    @Override
    public int priority() {
        return 99;
    }

    @Override
    public boolean supportsMediaType(MediaType mediaType) {
        return supportedMediaTypes.contains(mediaType);
    }
}
