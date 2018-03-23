package io.lab.biblio.framework.view.converter;

import com.vaadin.data.Converter;
import com.vaadin.data.converter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class ConverterCache {

    private static final Logger logger = LoggerFactory.getLogger(ConverterCache.class);


    private static final Map<Class<?>, Converter<?,?>> CONVETERS_CACHE = new ConcurrentHashMap<>();


   static  {
        CONVETERS_CACHE.put(BigDecimal.class, new StringToBigDecimalConverter("Invalid value"));
        CONVETERS_CACHE.put(BigInteger.class, new StringToBigIntegerConverter("Invalid value"));

        CONVETERS_CACHE.put(Double.class, new StringToDoubleConverter("Invalid value"));
        CONVETERS_CACHE.put(Double.TYPE, new StringToDoubleConverter("Invalid value"));

        CONVETERS_CACHE.put(Float.class, new StringToFloatConverter("Invalid value"));
        CONVETERS_CACHE.put(Float.TYPE, new StringToFloatConverter("Invalid value"));

        CONVETERS_CACHE.put(Integer.class, new StringToIntegerConverter("Invalid value"));
        CONVETERS_CACHE.put(Integer.TYPE, new StringToIntegerConverter("Invalid value"));

        CONVETERS_CACHE.put(Long.class, new StringToLongConverter("Invalid value"));
        CONVETERS_CACHE.put(Long.TYPE, new StringToLongConverter("Invalid value"));

        CONVETERS_CACHE.put(Boolean.class, new StringToBooleanConverter("Invalid value"));
        CONVETERS_CACHE.put(Boolean.TYPE, new StringToBooleanConverter("Invalid value"));


    }



    public static <VALUE> Optional<Converter<?, VALUE>> getConverter(Class<VALUE> type) {
        return Optional.ofNullable((Converter<?, VALUE>) CONVETERS_CACHE.get(type));
    }
}
