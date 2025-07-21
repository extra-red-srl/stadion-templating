package it.extrared.stadion;

import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.input.InputData;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface TemplatingFacade<ID> {

    void applyTemplate(
            ID id, MediaType targetMediaType, OutputStream outputStream, InputData... inputs)
            throws ServiceNotFound, IOException, InvalidTemplateException;

    void applyTemplate(
            ID id,
            MediaType targetMediaType,
            OutputStream outputStream,
            MediaType inputMediaType,
            InputStream input)
            throws ServiceNotFound, IOException, InvalidTemplateException;

    void applyTemplateOnPojo(
            ID id, MediaType targetMediaType, OutputStream outputStream, Object input)
            throws ServiceNotFound, IOException, InvalidTemplateException;
}
