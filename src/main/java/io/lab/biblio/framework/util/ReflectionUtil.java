package io.lab.biblio.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by amazimpaka on 2018-03-23
 */
public final class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    private static final Map<Class<?>, Map<String, PropertyDescriptor>> PROPERTIES_CACHE = new HashMap<>();

    public static <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> readValues(Object object) {
        if (object == null) {
            return Collections.EMPTY_MAP;
        }

        try {
            final Map<String, Object> values = new HashMap<>();
            for (PropertyDescriptor pd : getPropertyDescriptors(object.getClass()).values()) {
                Object value = pd.getReadMethod().invoke(object, new Object[]{});
                values.put(pd.getName(), value);
            }
            return values;
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeValues(Object object, Map<String, Object> values) {
        if (object == null) {
            return;
        }

        try {
            for (PropertyDescriptor pd : getPropertyDescriptors(object.getClass()).values()) {
                if (values.containsKey(pd.getName())) {
                    try {
                        Object value = values.get(pd.getName());
                        pd.getWriteMethod().invoke(object, new Object[]{value});
                    } catch (IllegalArgumentException e) {
                        //Skip invalid values
                        logger.debug(e.getMessage(), e);
                    }
                }
            }

        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, PropertyDescriptor> getPropertyDescriptors(Class<?> type) {
        Map<String, PropertyDescriptor> properties = PROPERTIES_CACHE.computeIfAbsent(type, key -> {
            final Map<String, PropertyDescriptor> values = new HashMap<>();
            try {
                final BeanInfo beanInfo = Introspector.getBeanInfo(key);
                for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                    if (pd.getWriteMethod() != null
                            && pd.getReadMethod() != null
                            && !pd.getName().equals("class")) {
                        values.put(pd.getName(), pd);
                    }
                }
                return values;
            } catch (IntrospectionException e) {
                throw new RuntimeException(e);
            }
        });
        return properties;
    }

    public static Optional<PropertyDescriptor> getPropertyDescriptor(Class<?> type, String name) {
        final Map<String, PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(type);
        return Optional.ofNullable(propertyDescriptors.get(name));

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

   /* private static void setValue(Object object,PropertyDescriptor pd,String value){

                    Class<?> type = pd.getPropertyType();
                    Object value = null;

        convertValue(type, value);
    }

    private static void convertValue(Class<?> type, Object value) {
        if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
            value = Byte.valueOf(value);
        }

        else if (type.equals(Short.TYPE) || type.equals(Short.class)) {
            value = (short) RandomUtils.nextInt();
        }

        else if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            value = RandomUtils.nextInt();
        }

        else if (type.equals(Long.TYPE) || type.equals(Long.class)) {
            value = RandomUtils.nextLong();
        }

        else if (type.equals(Float.TYPE) || type.equals(Float.class)) {
            value = RandomUtils.nextFloat();
        }

        else if (type.equals(Double.TYPE) || type.equals(Double.class)) {
            value = RandomUtils.nextDouble();
        }

        else if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
            value = RandomUtils.nextBoolean();
        }


        else if (type.equals(Date.class)) {
            value = new Date(RandomUtils.nextLong());
        }

        else if (type.equals(LocalDateTime.class)) {
            value = LocalDateTime.of(1 + RANDOM.nextInt(2020),
                    Month.of(1 + RANDOM.nextInt(11)),
                    1 + RANDOM.nextInt(27),
                    1 + RANDOM.nextInt(23),
                    1 + RANDOM.nextInt(59),
                    1 + RANDOM.nextInt(59));
        }


        else if (type.equals(String.class)) {
            value = STRING_GENERATOR.generate(RANDOM.nextInt(25));
        }
    }*/
}
