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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.extrared.stadion.exceptions.InvalidTemplateException;
import it.extrared.stadion.exceptions.UnsupportedMediaTypeException;
import it.extrared.stadion.formats.MediaType;
import it.extrared.stadion.templating.node.StadionTemplate;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

/**
 * A {@link TemplateCatalog} decorator that caches compiled {@link StadionTemplate} instances so
 * that the underlying catalog (filesystem, classpath, database, …) is only consulted on cache
 * misses.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * TemplateCatalog<String> catalog = new CachingTemplateCatalog<>(new DirectoryTemplateCatalog("/templates"));
 *
 * // or with custom TTL / size:
 * TemplateCatalog<String> catalog = new CachingTemplateCatalog<>(new DirectoryTemplateCatalog("/templates"),
 *         500, Duration.ofMinutes(30));
 * }</pre>
 *
 * <p><strong>Cache invalidation:</strong>
 *
 * <ul>
 *   <li>Calling {@link #saveTemplate} automatically evicts any cached entry for the saved template.
 *   <li>For external updates (e.g. another process / cluster node changed the DB) call {@link
 *       #invalidate(Object)} or {@link #invalidateAll()} explicitly.
 *   <li>All entries expire automatically after the configured TTL (default: 10 minutes).
 * </ul>
 */
public class CachingTemplateCatalog<ID> extends DelegatingTemplateCatalog<ID> {

    private static final int DEFAULT_MAX_SIZE = 200;
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(10);

    private final Cache<CacheKey<ID>, StadionTemplate> cache;

    /**
     * Creates a caching catalog wrapping {@code delegate} with default settings (200 entries,
     * 10-minute TTL).
     *
     * @param delegate the underlying catalog to cache
     */
    public CachingTemplateCatalog(TemplateCatalog<ID> delegate) {
        this(delegate, DEFAULT_MAX_SIZE, DEFAULT_TTL);
    }

    /**
     * Creates a caching catalog with custom capacity and expiry settings.
     *
     * @param delegate the underlying catalog to cache
     * @param maxSize maximum number of compiled templates to keep in the cache
     * @param ttl time-to-live for each cache entry after it was last written
     */
    public CachingTemplateCatalog(TemplateCatalog<ID> delegate, int maxSize, Duration ttl) {
        super(delegate);
        this.cache = Caffeine.newBuilder().maximumSize(maxSize).expireAfterWrite(ttl).build();
    }

    // ── load ─────────────────────────────────────────────────────────────────

    @Override
    public StadionTemplate loadTemplateById(ID id, MediaType mediaType)
            throws UnsupportedMediaTypeException, IOException {
        CacheKey<ID> key = CacheKey.ofId(id, mediaType);
        StadionTemplate cached = cache.getIfPresent(key);
        if (cached != null) return cached;
        StadionTemplate template = super.loadTemplateById(id, mediaType);
        cache.put(key, template);
        return template;
    }

    @Override
    public StadionTemplate loadTemplateByName(String templateName, MediaType mediaType)
            throws InvalidTemplateException, IOException {
        CacheKey<ID> key = CacheKey.ofName(templateName, mediaType);
        StadionTemplate cached = cache.getIfPresent(key);
        if (cached != null) return cached;
        StadionTemplate template = super.loadTemplateByName(templateName, mediaType);
        cache.put(key, template);
        return template;
    }

    // ── write ─────────────────────────────────────────────────────────────────

    /**
     * Delegates to the underlying catalog and then evicts any cache entry that matches either the
     * template id or name returned by the delegate, covering both load paths.
     */
    @Override
    public TemplateMetadata<ID> saveTemplate(byte[] content, TemplateMetadata<ID> metadata)
            throws InvalidTemplateException, IOException {
        TemplateMetadata<ID> result = super.saveTemplate(content, metadata);
        evict(result.getId(), result.getName());
        return result;
    }

    // ── explicit invalidation ─────────────────────────────────────────────────

    /** Evicts all cached entries whose id equals {@code id}. */
    public void invalidate(ID id) {
        evict(id, null);
    }

    /** Evicts all cached entries whose name equals {@code name}. */
    public void invalidateByName(String name) {
        evict(null, name);
    }

    /** Evicts the entire cache. */
    public void invalidateAll() {
        cache.invalidateAll();
    }

    // ── internal ──────────────────────────────────────────────────────────────

    private void evict(ID id, String name) {
        List<CacheKey<ID>> toEvict =
                cache.asMap().keySet().stream()
                        .filter(k -> k.matchesId(id) || k.matchesName(name))
                        .toList();
        cache.invalidateAll(toEvict);
    }

    // ── cache key ─────────────────────────────────────────────────────────────

    /**
     * Immutable cache key that discriminates between id-based and name-based loads for the same
     * media type.
     */
    private record CacheKey<ID>(ID id, String name, String mediaTypeMime) {

        static <ID> CacheKey<ID> ofId(ID id, MediaType mediaType) {
            return new CacheKey<>(id, null, mediaType.asMime());
        }

        static <ID> CacheKey<ID> ofName(String name, MediaType mediaType) {
            return new CacheKey<>(null, name, mediaType.asMime());
        }

        boolean matchesId(ID targetId) {
            return targetId != null && targetId.equals(id);
        }

        boolean matchesName(String targetName) {
            return targetName != null && targetName.equals(name);
        }
    }
}
