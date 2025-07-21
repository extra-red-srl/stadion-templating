package it.extrared.stadion;

import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.formats.MediaType;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public class ServiceProvider {

    public static <T extends MediaTypeHandlerService> T getServiceByMediaType(
            Class<T> serviceType, MediaType mediaType) throws ServiceNotFound {
        ServiceLoader<T> serviceLoader = ServiceLoader.load(serviceType);
        Iterator<T> services = serviceLoader.iterator();
        List<T> candidates = new ArrayList<>();
        while (services.hasNext()) {
            T srv = services.next();
            if (srv.supportsMediaType(mediaType)) candidates.add(srv);
        }
        candidates.stream().sorted(Comparator.comparing(T::priority));
        if (candidates.isEmpty())
            throw new ServiceNotFound(
                    "No service found for type %s supporting %s."
                            .formatted(serviceType.getSimpleName(), mediaType.asMime()));
        return candidates.get(0);
    }

    static <T> T firstOrDefault(Class<T> serviceType, Supplier<T> def) {
        return ServiceLoader.load(serviceType).findFirst().orElse(def.get());
    }
}
