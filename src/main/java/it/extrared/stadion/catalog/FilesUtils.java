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

import it.extrared.stadion.formats.TemplateType;
import it.extrared.stadion.utils.CommonUtils;
import java.nio.file.Path;
import java.util.function.Predicate;

/** Utility methods for path-based {@link TemplateCatalog} operations. */
public class FilesUtils {

    /**
     * Builds a {@link java.util.function.Predicate} that filters {@link java.nio.file.Path} values
     * according to the criteria in {@code params}.
     *
     * @param params search parameters; name and template-type filters are applied when non-null
     * @return a predicate that returns {@code true} for matching paths
     */
    public static Predicate<Path> toPredicate(SearchParams params) {
        return p -> {
            boolean result = true;
            if (CommonUtils.hasText(params.getName()))
                result = p.toString().toLowerCase().contains(params.getName().toLowerCase());
            if (result && params.getTemplateType() != null)
                result = p.toString().endsWith(params.getTemplateType().name().toLowerCase());
            return result;
        };
    }

    /**
     * Assembles the full file name (name + extension) for the given template metadata.
     *
     * @param metadata the template metadata containing name and type; must not be {@code null}
     * @return the full file name, e.g. {@code "myTemplate.json"}
     */
    public static String createFullFileName(TemplateMetadata<String> metadata) {
        return metadata.getName()
                .concat(".")
                .concat(metadata.getTemplateType().name().toLowerCase());
    }

    /**
     * Parses a file name string into a {@link TemplateMetadata} instance.
     *
     * <p>The string must follow the pattern {@code <name>.<extension>}, where the extension matches
     * a {@link it.extrared.stadion.formats.TemplateType} name.
     *
     * @param str the file name string to parse
     * @return the corresponding metadata
     */
    public static TemplateMetadata<String> asMetadata(String str) {
        TemplateMetadata<String> metadata = new TemplateMetadata<>();
        metadata.setId(str);
        String[] fileArr = str.split("\\.");
        metadata.setName(fileArr[0]);
        metadata.setTemplateType(TemplateType.valueOf(fileArr[fileArr.length - 1].toUpperCase()));
        return metadata;
    }
}
