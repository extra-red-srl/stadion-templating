package it.extrared.stadion.catalog;

public class SaveResult {

    private final Object result;

    private SaveResult(Object t) {
        this.result = t;
    }

    public Object getResult() {
        return result;
    }

    public <T> T getResult(Class<T> type) {
        if (result == null) return null;
        else if (!type.isAssignableFrom(result.getClass())) return type.cast(result);
        throw new UnsupportedOperationException(
                "Cannot convert %s to %s"
                        .formatted(result.getClass().getSimpleName(), type.getSimpleName()));
    }

    public static SaveResult of(Object obj) {
        return new SaveResult(obj);
    }
}
