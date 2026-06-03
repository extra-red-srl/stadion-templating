package it.extrared.stadion.catalog;

import it.extrared.stadion.formats.TemplateType;
import it.extrared.stadion.utils.CommonUtils;
import java.nio.file.Path;
import java.util.function.Predicate;

public class FilesUtils {

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

    public static String createFullFileName(TemplateMetadata<String> metadata) {
        return metadata.getName()
                .concat(".")
                .concat(metadata.getTemplateType().name().toLowerCase());
    }

    public static TemplateMetadata<String> asMetadata(String str) {
        TemplateMetadata<String> metadata = new TemplateMetadata<>();
        metadata.setId(str);
        String[] fileArr = str.split("\\.");
        metadata.setName(fileArr[0]);
        metadata.setTemplateType(TemplateType.valueOf(fileArr[fileArr.length - 1].toUpperCase()));
        return metadata;
    }
}
