package fr.umlv.java.inside;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JsonPrinter {

    private static String propertyName(String name) {
        return Character.toLowerCase(name.charAt(3)) + name.substring(4);
    }

    static public String toJson(Object object) {
        return Arrays.stream(object.getClass().getMethods())
                .filter( method -> method.getName().startsWith("get") )
                .map( method -> propertyName(method.getName()))
                .collect(Collectors.joining(", ", "{", "}"));
    }

}
