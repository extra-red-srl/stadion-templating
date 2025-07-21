package it.extrared.stadion.templating.directive.property;

import it.extrared.stadion.exceptions.TemplatingException;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/** A property name part of a property directive. */
public class PropertyName implements PropertyPart {

    private String propertyName;

    public PropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public Object evaluate(Object o) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());
        Optional<PropertyDescriptor> descriptor = selectDescriptorByName(propertyName, beanInfo);
        PropertyDescriptor desc =
                descriptor.orElseThrow(
                        () -> {
                            StringBuilder msg =
                                    new StringBuilder("No property found with name %s in type %s.");
                            if (o instanceof Collection || (o != null && o.getClass().isArray()))
                                msg.append(
                                        "The property name received a collection as the context. Check if your are using a template meant for a single resource against a collection.");
                            return new TemplatingException(
                                    msg.toString()
                                            .formatted(propertyName, o.getClass().getSimpleName()));
                        });
        try {
            return desc.getReadMethod().invoke(o);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<PropertyDescriptor> selectDescriptorByName(String name, BeanInfo beanInfo) {
        return selectDescriptor(
                pd -> pd.getName().toUpperCase().equals(name.toUpperCase()), beanInfo);
    }

    public Optional<PropertyDescriptor> selectDescriptor(
            Predicate<PropertyDescriptor> namePred, BeanInfo beanInfo) {
        return Stream.of(beanInfo.getPropertyDescriptors())
                .filter(d -> namePred.test(d))
                .findFirst();
    }
}
