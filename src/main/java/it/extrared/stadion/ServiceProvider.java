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
package it.extrared.stadion;

import it.extrared.stadion.exceptions.ServiceNotFound;
import it.extrared.stadion.formats.MediaType;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Central registry for SPI services that implement {@link MediaTypeHandlerService}.
 *
 * <p>Services are discovered once via {@link ServiceLoader} and then cached per service type.
 * Subsequent lookups filter the cached list without touching the classpath scanner.
 */
public class ServiceProvider {

    /**
     * Cache of all providers discovered for each service type. {@link ServiceLoader} scans the
     * classpath only once per type; subsequent calls filter and sort the already-loaded list.
     *
     * <p>The cached list is an immutable snapshot, so concurrent reads are safe without locking.
     */
    private static final ConcurrentHashMap<Class<?>, List<?>> SERVICE_CACHE =
            new ConcurrentHashMap<>();

    /**
     * Returns the highest-priority service of type {@code serviceType} that supports {@code
     * mediaType}.
     *
     * @param <T> the service type
     * @param serviceType the service interface class
     * @param mediaType the media type to match
     * @return the best matching service
     * @throws ServiceNotFound if no registered service supports the given media type
     */
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
