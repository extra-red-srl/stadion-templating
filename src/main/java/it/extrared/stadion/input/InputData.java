package it.extrared.stadion.input;

import it.extrared.stadion.formats.MediaType;
import java.io.InputStream;

public class InputData {

    private MediaType mediaType;

    private InputStream input;

    private InputData(MediaType mediaType, InputStream input) {
        this.mediaType = mediaType;
        this.input = input;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public InputStream getInput() {
        return input;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private MediaType mediaType;

        private InputStream input;

        public Builder mediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        public Builder input(InputStream input) {
            this.input = input;
            return this;
        }

        public InputData build() {
            return new InputData(mediaType, input);
        }
    }
}
