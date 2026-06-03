package it.extrared.stadion.templating.directive;

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.templating.directive.factory.TemplateDirectiveFactory;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DirectiveProvider {

    /**
     * All directive factories indexed by name, loaded once via {@link ServiceLoader}. Lookup is
     * O(1) and the map is immutable after construction, making it safe for concurrent use.
     */
    private static final Map<String, TemplateDirectiveFactory> FACTORIES_BY_NAME;

    static {
        ServiceLoader<TemplateDirectiveFactory> loader =
                ServiceLoader.load(TemplateDirectiveFactory.class);
        FACTORIES_BY_NAME =
                StreamSupport.stream(loader.spliterator(), false)
                        .collect(
                                Collectors.toUnmodifiableMap(
                                        f -> f.getInfo().getName(),
                                        f -> f,
                                        // keep first on name collision
                                        (a, b) -> a));
    }

    public TemplateDirective createDirective(String name, Object... params) {
        TemplateDirectiveFactory factory = FACTORIES_BY_NAME.get(name);
        if (factory == null)
            throw new TemplatingException(
                    "No template directive found with name %s".formatted(name));
        return factory.createDirective(params);
    }

    public Iterator<TemplateDirectiveFactory> spiIterator() {
        return FACTORIES_BY_NAME.values().iterator();
    }
}
