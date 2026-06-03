package it.extrared.stadion.catalog;

public class TemplateCatalogException extends RuntimeException {
    public TemplateCatalogException(String message) {
        super(message);
    }

    public TemplateCatalogException(Exception e) {
        super(e);
    }
}
