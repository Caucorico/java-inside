package fr.umlv.java.inside;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class JsonPrinter {

    private final static ClassValue<Method[]> cache = new ClassValue<>() {
        @Override
        protected Method[] computeValue(Class<?> dynamicClass) {
            return dynamicClass.getMethods();
        }
    };

    private final static ClassValue<Stream<Map.Entry<String, Method>>> betterCache = new ClassValue<>() {
        @Override
        protected Stream<Map.Entry<String, Method>> computeValue(Class<?> dynamicClass) {
            return Arrays.stream(dynamicClass.getMethods())
                    .filter(method -> method.getName().startsWith("get"))
                    .filter(method -> method.isAnnotationPresent(JSONProperty.class))
                    .sorted(Comparator.comparing(Method::getName))
                    .map( method -> {
                        String methodName;
                        if ( !method.getAnnotation(JSONProperty.class).value().isEmpty() ) {
                            methodName = method.getAnnotation(JSONProperty.class).value();
                        } else {
                            methodName = propertyName(method.getName());
                        }
                        return Map.entry(methodName, method);
                    });
        }
    };

    private static String propertyName(String name) {
        return Character.toLowerCase(name.charAt(3)) + name.substring(4);
    }

    private static Object callGetter(Object o, Method getter) {
        try {
            return getter.invoke(o);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            var cause = e.getCause();
            if ( cause instanceof RuntimeException ) {
                throw (RuntimeException) cause;
            }
            if ( cause instanceof Error ) {
                throw (Error) cause;
            }
            throw new UndeclaredThrowableException(cause);
        }
    }

    public static String toJson(Object object) {
        return betterCache.get(object.getClass())
                .map(methodTuple -> "\"" + methodTuple.getKey() + "\": \"" + callGetter(object, methodTuple.getValue()) + "\"")
                .collect(joining(",\n", "{\n", "\n}"));
    }

}
