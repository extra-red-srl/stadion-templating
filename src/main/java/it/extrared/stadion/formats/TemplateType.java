package it.extrared.stadion.formats;

import it.extrared.stadion.exceptions.UnsupportedMediaTypeException;
import java.util.Arrays;

public enum TemplateType {
    JSON,
    XML;

    public boolean isCompatibleWith(MediaType mediaType) {
        return mediaType.getSubType().contains(name().toLowerCase());
    }

    public static TemplateType getSupporting(MediaType mediaType)
            throws UnsupportedMediaTypeException {
        return Arrays.stream(values())
                .filter(v -> v.isCompatibleWith(mediaType))
                .findFirst()
                .orElseThrow(
                        () ->
                                new UnsupportedMediaTypeException(
                                        "MediaType %s has no supported template type."
                                                .formatted(mediaType.asMime())));
    }
}
