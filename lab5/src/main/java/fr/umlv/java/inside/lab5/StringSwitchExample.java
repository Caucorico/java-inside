package fr.umlv.java.inside.lab5;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;

public class StringSwitchExample {

    private static final MethodHandle STRING_EQUALS;

    static {
        var lookup = MethodHandles.lookup();
        try {
            STRING_EQUALS = lookup.findVirtual(String.class, "equals", MethodType.methodType(boolean.class, Object.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    public static int stringSwitch(String s) {
        Objects.requireNonNull(s);
        return switch (s) {
            case "foo" -> 0;
            case "bar" -> 1;
            case "bazz" -> 2;
            default -> -1;
        };
    }

    public static MethodHandle createMHFromStrings2(String ... values ) {
        var mh = MethodHandles.dropArguments(MethodHandles.constant(int.class, -1), 0, String.class);
        for ( var i = 0 ; i < values.length ; i++ ) {
            mh = MethodHandles.guardWithTest(
                    MethodHandles.insertArguments(STRING_EQUALS, 1, values[i]),
                    MethodHandles.dropArguments(MethodHandles.constant(int.class, i), 0, String.class),
                    mh
            );
        }
        return mh;
    }

    public static int stringSwitch2(String s) {
        var mh = createMHFromStrings2("foo", "bar", "bazz");
        try {
            return (int)mh.invokeExact(s);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
