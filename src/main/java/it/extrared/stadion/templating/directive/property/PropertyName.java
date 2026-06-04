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
package it.extrared.stadion.templating.directive.property;

import it.extrared.stadion.exceptions.TemplatingException;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/** A property name part of a property directive. */
public class PropertyName implements PropertyPart {

    /** Property name normalised to lowercase at construction time — never recomputed. */
    private final String propertyName;

    /**
     * Resolved lazily on the first {@link #evaluate} call and cached for all subsequent ones.
     * Declared {@code volatile} so that the single write is visible to all threads without locking;
     * worst-case two threads resolve the same handle concurrently on first access, which is
     * harmless since the result is always identical.
     */
    private volatile MethodHandle getter;

    public PropertyName(String propertyName) {
        this.propertyName = propertyName.toLowerCase(Locale.ROOT);
    }

    @Override
    public Object evaluate(Object o) throws IntrospectionException {
        MethodHandle mh = getter;
        if (mh == null) {
            BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());
            PropertyDescriptor desc =
                    selectDescriptorByName(propertyName, beanInfo)
                            .orElseThrow(() -> newTemplatingEx(o, propertyName));
            try {
                Method readMethod = desc.getReadMethod();
                readMethod.setAccessible(true);
                mh =
                        MethodHandles.lookup()
                                .unreflect(readMethod)
                                .asType(MethodType.methodType(Object.class, Object.class));
                getter = mh;
            } catch (IllegalAccessException e) {
                throw new TemplatingException(e);
            }
        }
        try {
            return mh.invoke(o);
        } catch (Throwable e) {
            throw new TemplatingException(e);
        }
    }

    private TemplatingException newTemplatingEx(Object o, String propertyName) {
        StringBuilder msg = new StringBuilder("No property found with name %s in type %s.");
        if (o instanceof Collection || (o != null && o.getClass().isArray()))
            msg.append(
                    " The property name received a collection as the context."
                            + " Check if you are using a template meant for a single resource against a collection.");
        return new TemplatingException(
                msg.toString()
                        .formatted(propertyName, o != null ? o.getClass().getSimpleName() : null));
    }

    public Optional<PropertyDescriptor> selectDescriptorByName(String name, BeanInfo beanInfo) {
        return selectDescriptor(pd -> pd.getName().toLowerCase(Locale.ROOT).equals(name), beanInfo);
    }

    public Optional<PropertyDescriptor> selectDescriptor(
            Predicate<PropertyDescriptor> namePred, BeanInfo beanInfo) {
        return Stream.of(beanInfo.getPropertyDescriptors()).filter(namePred).findFirst();
    }
}
