package it.extrared.stadion.catalog;

import static it.extrared.stadion.catalog.FilesUtils.asMetadata;
import static it.extrared.stadion.catalog.FilesUtils.toPredicate;

import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.UnsupportedMediaTypeException;
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

    private static final String TEMPLATES_DIR = "/templates/";

    @Override
    protected InputStream getTemplateStream(String s)
            throws UnsupportedMediaTypeException, IOException {
        return getClass().getResourceAsStream(TEMPLATES_DIR.concat(s));
    }

    @Override
    protected InputStream getTemplateStream(String templateName, TemplateType templateType) {
        return getClass()
                .getResourceAsStream(
                        TEMPLATES_DIR.concat(
                                templateName
                                        .concat(".")
                                        .concat(templateType.name().toLowerCase())));
    }

    @Override
    public byte[] loadTemplateContent(String s) {
        try (InputStream is = getClass().getResourceAsStream(TEMPLATES_DIR.concat(s))) {
            assert is != null;
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        try {
            URL resource = getClass().getResource(TEMPLATES_DIR.concat(s));
            if (resource != null) {
                Path path = Paths.get(resource.toURI());
                if (Files.exists(path)) {
                    TemplateMetadata<String> metadata = new TemplateMetadata<>();
                    metadata.setId(s);
                    String[] fileArr = s.split("\\.");
                    metadata.setName(fileArr[0]);
                    metadata.setTemplateType(TemplateType.valueOf(fileArr[1].toUpperCase()));
                    return metadata;
                }
            }
        } catch (Exception e) {
            throw new TemplateCatalogException(e);
        }
        return null;
    }

    @Override
    public List<TemplateMetadata<String>> searchTemplates(SearchParams searchParams) {
        try (Stream<Path> paths =
                Files.list(
                        Paths.get(
                                Objects.requireNonNull(getClass().getResource("/templates"))
                                        .toURI()))) {
            return paths.filter(p -> toPredicate(searchParams).test(p))
                    .map(p -> asMetadata(p.getFileName().toString()))
                    .toList();
        } catch (Exception e) {
            throw new TemplateCatalogException(e);
        }
    }
}
