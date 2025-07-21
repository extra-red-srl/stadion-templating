package it.extrared.stadion.formats;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class MediaType {

    public static final MediaType A_JSON = MediaType.ofApplication("json");

    public static final MediaType T_JSON = MediaType.ofText("json");

    public static final MediaType A_JSON_LD = MediaType.ofApplication("ld+json");

    public static final MediaType A_XML = MediaType.ofApplication("xml");

    public static final MediaType T_XML = MediaType.ofText("xml");

    public static final MediaType A_XHTML = MediaType.ofApplication("xhtml");

    public static final MediaType T_XHTML = MediaType.ofText("xhtml");

    private static final Set<String> SUPPORTED =
            Set.of(A_JSON.subType, A_XML.subType, A_XHTML.subType, A_JSON_LD.subType);

    private String type;

    private String subType;

    MediaType(String type, String subType) {
        this.type = type;
        this.subType = cleanSubType(subType);
    }

    MediaType(String mediaType) {
        String[] arr = mediaType.split(Pattern.quote("/"));
        if (arr.length > 1) {
            this.type = arr[0];
            this.subType = cleanSubType(arr[1]);
        } else {
            throw new UnsupportedOperationException(
                    "Value %s is not a valid media type".formatted(mediaType));
        }
    }

    private String cleanSubType(String subType) {
        int i = subType.indexOf(";");
        if (i != -1) subType = subType.substring(0, i);
        return subType;
    }

    public String asMime() {
        return type.concat("/").concat(subType);
    }

    public String getType() {
        return type;
    }

    public MediaType setType(String type) {
        this.type = type;
        return this;
    }

    public String getSubType() {
        return subType;
    }

    public MediaType setSubType(String subType) {
        this.subType = subType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaType mediaType = (MediaType) o;
        return Objects.equals(type, mediaType.type) && Objects.equals(subType, mediaType.subType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, subType);
    }

    public static MediaType ofText(String subType) {
        return new MediaType("text", subType);
    }

    public static MediaType ofApplication(String subType) {
        return new MediaType("application", subType);
    }

    public static MediaType of(String type, String subType) {
        return new MediaType(type, subType);
    }

    public static MediaType of(String mediaType) {
        return new MediaType(mediaType);
    }

    public static boolean supportsSubType(String subType) {
        return SUPPORTED.contains(subType);
    }
}
