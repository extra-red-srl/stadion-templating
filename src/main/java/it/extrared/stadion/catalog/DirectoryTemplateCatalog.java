/*
 * Copyright 2026 Extrared
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.extrared.stadion.catalog;

import static it.extrared.stadion.catalog.FilesUtils.asMetadata;
import static it.extrared.stadion.catalog.FilesUtils.createFullFileName;
import static it.extrared.stadion.catalog.FilesUtils.toPredicate;

import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.UnsupportedMediaTypeException;
import it.extrared.stadion.formats.TemplateType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * {@link TemplateCatalog} implementation that reads templates from a directory on the local
 * filesystem.
 *
 * <p>Template files are looked up by their full filename (e.g. {@code myTemplate.json}). The
 * directory must exist and be readable at construction time. Saving a template writes a new file
 * into the same directory.
 */
public class DirectoryTemplateCatalog extends AbstractTemplateCatalog<String> {

    private final Path path;

    public DirectoryTemplateCatalog(String rootDir) {
        this(Path.of(rootDir));
    }

    public DirectoryTemplateCatalog(URI uri) {
        this(Path.of(uri));
    }

    public DirectoryTemplateCatalog(Path path) {
        this.path = path;
    }

    @Override
    public byte[] loadTemplateContent(String id) {
        try {
            return Files.readAllBytes(path.resolve(id));
        } catch (IOException e) {
            throw new TemplateCatalogException(e);
        }
    }

    @Override
    public TemplateMetadata<String> saveTemplate(byte[] template, TemplateMetadata<String> metadata)
            throws InvalidTemplateException, IOException {
        validate(metadata);
        String fullFileName = createFullFileName(metadata);
        metadata.setId(fullFileName);
        Path file = path.resolve(fullFileName);
        Files.write(file, template);

        return metadata;
    }

    @Override
    public TemplateMetadata<String> findOne(String id) {
        Path file = path.resolve(id);
        if (!Files.exists(file)) {
            throw new TemplateCatalogException("No template found for %s".formatted(id));
        }
        TemplateMetadata<String> metadata = new TemplateMetadata<>();
        metadata.setId(id);
        String[] fileArr = id.split("\\.");
        metadata.setName(fileArr[0]);
        metadata.setTemplateType(TemplateType.valueOf(fileArr[fileArr.length - 1].toUpperCase()));
        return metadata;
    }

    @Override
    public List<TemplateMetadata<String>> searchTemplates(SearchParams searchParams) {
        try (Stream<Path> paths = Files.list(path)) {
            return paths.filter(p -> toPredicate(searchParams).test(p))
                    .map(p -> asMetadata(p.getFileName().toString()))
                    .toList();
        } catch (IOException e) {
            throw new TemplateCatalogException(e);
        }
    }

    @Override
    protected InputStream getTemplateStream(String id)
            throws UnsupportedMediaTypeException, IOException {
        return load(id);
    }

    @Override
    protected InputStream getTemplateStream(String templateName, TemplateType templateType)
            throws IOException {
        return load(templateName.concat(".").concat(templateType.name().toLowerCase()));
    }

    private InputStream load(String fileName) throws IOException {
        Path file = path.resolve(fileName);
        if (!Files.exists(file)) {
            throw new IOException("No template found with file name %s".formatted(fileName));
        }
        return Files.newInputStream(file);
    }
}
