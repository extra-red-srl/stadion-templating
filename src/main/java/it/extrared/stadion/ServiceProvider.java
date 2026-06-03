package it.extrared.stadion;

import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.formats.MediaType;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class ServiceProvider {

    /**
     * Cache of all providers discovered for each service type. {@link ServiceLoader} scans the
     * classpath only once per type; subsequent calls filter and sort the already-loaded list.
     *
     * <p>The cached list is an immutable snapshot, so concurrent reads are safe without locking.
     */
    private static final ConcurrentHashMap<Class<?>, List<?>> SERVICE_CACHE =
            new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends MediaTypeHandlerService> T getServiceByMediaType(
            Class<T> serviceType, MediaType mediaType) throws ServiceNotFound {
        List<T> all =
                (List<T>)
                        SERVICE_CACHE.computeIfAbsent(
                                serviceType,
                                type -> {
                                    // ServiceLoader.load() is only executed on first access per
                                    // type
                                    List<T> discovered = discovery(serviceType);
                                    return List.copyOf(discovered);
                                });

        List<T> candidates =
                all.stream()
                        .filter(srv -> srv.supportsMediaType(mediaType))
                        // higher priority number wins; get(0) returns the best match
                        .sorted(Comparator.comparing(T::priority).reversed())
                        .toList();

        if (candidates.isEmpty())
            throw new ServiceNotFound(
                    "No service found for type %s supporting %s."
                            .formatted(serviceType.getSimpleName(), mediaType.asMime()));
        return candidates.getFirst();
    }

    private static <T> List<T> discovery(Class<T> srvType) {
        ServiceLoader<T> loader = ServiceLoader.load(srvType);
        return loader.stream().map(ServiceLoader.Provider::get).toList();
    }

    static <T> T firstOrDefault(Class<T> serviceType, Supplier<T> def) {
        return ServiceLoader.load(serviceType).findFirst().orElse(def.get());
    }
}
