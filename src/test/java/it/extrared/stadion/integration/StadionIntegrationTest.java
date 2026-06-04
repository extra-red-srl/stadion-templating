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
package it.extrared.stadion.integration;

import com.github.marschall.memoryfilesystem.MemoryFileSystemBuilder;
import it.extrared.stadion.catalog.TemplateCatalog;
import it.extrared.stadion.catalog.TemplateMetadata;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.formats.TemplateType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class StadionIntegrationTest {

    private FileSystem fileSystem;
    protected Path root;

    @BeforeEach
    public void open() throws IOException {
        this.fileSystem = MemoryFileSystemBuilder.newEmpty().build("stadion-templates");
        this.root = fileSystem.getPath("root");
        createDirectoryIfNotExists(root, root.getFileName().toString());
    }

    @AfterEach
    public void close() throws IOException {
        this.fileSystem.close();
    }

    protected byte[] resourceAsBytes(String resName) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resName)) {
            assert is != null;
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            return bytes;
        }
    }

    protected String saveTemplate(
            TemplateCatalog<String> templateCatalog,
            String templateResName,
            TemplateType templateType)
            throws IOException, InvalidTemplateException {
        TemplateMetadata<String> metadata = new TemplateMetadata<>();
        metadata.setName(templateResName);
        metadata.setTemplateType(templateType);
        String fileName = templateResName.concat(".").concat(templateType.name().toLowerCase());
        byte[] template = resourceAsBytes(fileName);
        return templateCatalog.saveTemplate(template, metadata).getId();
    }

    protected void createDirectoryIfNotExists(Path file, String name) {
        try {
            if (!Files.exists(file)) Files.createDirectories(file);
        } catch (Exception e) {
            throw new RuntimeException(
                    "La directory %s non esiste e l'applicazione non riesce a crearla."
                            .formatted(name),
                    e);
        }
    }
}
