package it.extrared.stadion.templating.directive;

import it.extrared.stadion.exceptions.TemplatingException;
import it.extrared.stadion.templating.directive.factory.DirectiveInfo;
import it.extrared.stadion.templating.directive.factory.TemplateDirectiveFactory;
import java.util.Iterator;
import java.util.ServiceLoader;

public class DirectiveProvider {

    public TemplateDirective createDirective(String name, Object... params) {
        ServiceLoader<TemplateDirectiveFactory> srvLoader =
                ServiceLoader.load(TemplateDirectiveFactory.class);
        for (TemplateDirectiveFactory spi : srvLoader) {
            DirectiveInfo info = spi.getInfo();
            if (info.getName().equals(name)) return spi.createDirective(params);
        }
        throw new TemplatingException("No template directive found with name %s".formatted(name));
    }

    public Iterator<TemplateDirectiveFactory> spiIterator() {
        ServiceLoader<TemplateDirectiveFactory> srvLoader =
                ServiceLoader.load(TemplateDirectiveFactory.class);
        return srvLoader.iterator();
    }
}
