package fr.umlv.java.inside;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JsonPrinter {

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
            } else if ( cause instanceof Error ){
                throw (Error) cause;
            } else {
                throw new UndeclaredThrowableException(cause);
            }
        }
    }

    static public String toJson(Object object) {
        return Arrays.stream(object.getClass().getMethods())
                .filter( method -> method.getName().startsWith("get") )
                .map( method -> "\"" +  propertyName(method.getName()) + "\": \"" + callGetter(object, method) + "\"")
                .collect(Collectors.joining(",\n", "{\n", "\n}"));
    }

}
