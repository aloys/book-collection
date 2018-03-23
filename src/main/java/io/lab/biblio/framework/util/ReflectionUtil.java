package io.lab.biblio.framework.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Created by amazimpaka on 2018-03-23
 */
public final class ReflectionUtil {

    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        }   catch (IllegalAccessException  | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<Class<?>> resolveGeneric(Class<?> analyzedClass, int index) {
        final ParameterizedType genericSuperclass = (ParameterizedType) analyzedClass.getGenericSuperclass();
        if (genericSuperclass != null) {
            final Type[] arguments = genericSuperclass.getActualTypeArguments();

            if (arguments != null && arguments.length >= index + 1) {
                final Type type = arguments[index];
                if (type.getClass() == Class.class) {
                    return Optional.ofNullable((Class<?>) type);
                }
            }
        }
        return Optional.empty();
    }
}
