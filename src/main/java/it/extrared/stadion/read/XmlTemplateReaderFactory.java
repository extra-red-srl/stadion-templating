package it.extrared.stadion.read;

import it.extrared.stadion.formats.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class XmlTemplateReaderFactory extends AbstractTemplateReaderFactory {

    private static final List<MediaType> SUPPORTED_MEDIA_TYPES =
            Arrays.asList(MediaType.A_XML, MediaType.T_XML);

    public XmlTemplateReaderFactory() {
        super(SUPPORTED_MEDIA_TYPES);
    }

    @Override
    public TemplateReader createReader(InputStream inputStream) throws IOException {
        return new XmlTemplateReader(inputStream);
    }
}
