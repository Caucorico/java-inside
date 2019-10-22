package fr.umlv.java.inside.lab5;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;

public class StringSwitchExample {
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
        return null;
    }

    public static int stringSwitch2(String s) {
        var mh = createMHFromStrings2("foo", "bar", "bazz");
        try {
            return (Integer)mh.invokeExact(s);
        } catch (RuntimeException e) {
            throw (RuntimeException) e;
        } catch ( Error e ) {
            throw (Error) e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
