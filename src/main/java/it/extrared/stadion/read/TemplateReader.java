package it.extrared.stadion.read;

import it.extrared.stadion.templating.node.StadionTemplate;
import java.io.Closeable;
import java.io.IOException;

public interface TemplateReader extends Closeable {

    StadionTemplate readTemplate() throws IOException;

    void close() throws IOException;
}
