package it.extrared.stadion.catalog;

import static it.extrared.stadion.catalog.FilesUtils.asMetadata;
import static it.extrared.stadion.catalog.FilesUtils.toPredicate;

import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.formats.TemplateType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ResourcesTemplateCatalog extends AbstractTemplateCatalog<String> {

    private static final String TEMPLATES_DIR = "/stadion-templates";

    @Override
    protected InputStream getTemplateStream(String s) throws IOException {
        InputStream is = getClass().getResourceAsStream(TEMPLATES_DIR + "/" + s);
        if (is == null) throw new IOException("Template resource not found: " + s);
        return is;
    }

    @Override
    protected InputStream getTemplateStream(String templateName, TemplateType templateType)
            throws IOException {
        String resourcePath =
                TEMPLATES_DIR
                        + "/"
                        + templateName.concat(".").concat(templateType.name().toLowerCase());
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) throw new IOException("Template resource not found: " + resourcePath);
        return is;
    }

    @Override
    public byte[] loadTemplateContent(String s) {
        try (InputStream is = getTemplateStream(s)) {
            return is.readAllBytes();
        } catch (IOException e) {
            throw new TemplateCatalogException(e);
        }
    }

    @Override
    public TemplateMetadata<String> saveTemplate(byte[] content, TemplateMetadata<String> metadata)
            throws InvalidTemplateException, IOException {
        throw new UnsupportedOperationException(
                "Resource directory catalog doesn't support saving a template");
    }

    @Override
    public TemplateMetadata<String> findOne(String s) {
        try (InputStream testStream = getClass().getResourceAsStream(TEMPLATES_DIR + "/" + s)) {
            if (testStream != null) {
                TemplateMetadata<String> metadata = new TemplateMetadata<>();
                metadata.setId(s);
                String[] fileArr = s.split("\\.");
                metadata.setName(fileArr[0]);
                metadata.setTemplateType(
                        TemplateType.valueOf(fileArr[fileArr.length - 1].toUpperCase()));
                return metadata;
            }
        } catch (IOException e) {
            throw new TemplateCatalogException(e);
        }
        throw new TemplateCatalogException("Template resource not found: " + s);
    }

    @Override
    public List<TemplateMetadata<String>> searchTemplates(SearchParams searchParams) {
        try {
            URL dirUrl =
                    Objects.requireNonNull(
                            getClass().getResource(TEMPLATES_DIR),
                            "Templates directory not found: " + TEMPLATES_DIR);
            if ("jar".equals(dirUrl.getProtocol())) {
                throw new UnsupportedOperationException(
                        "searchTemplates is not supported when resources are packaged in a JAR");
            }
            try (Stream<Path> paths = Files.list(Paths.get(dirUrl.toURI()))) {
                return paths.filter(p -> toPredicate(searchParams).test(p))
                        .map(p -> asMetadata(p.getFileName().toString()))
                        .toList();
            }
        } catch (UnsupportedOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new TemplateCatalogException(e);
        }
    }
}
