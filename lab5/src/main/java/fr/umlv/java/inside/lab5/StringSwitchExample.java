package fr.umlv.java.inside.lab5;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
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

    public static int stringSwitch3(String s) {
        var mh = createMHFromStrings3("foo", "bar", "bazz");
        try {
            return (int)mh.invokeExact(s);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public static MethodHandle createMHFromStrings3(String... matches) {
        return new InliningCache(matches).dynamicInvoker();
    }

    static class InliningCache extends MutableCallSite {
        private static final MethodHandle SLOW_PATH;
        static {
            try {
                SLOW_PATH = MethodHandles.lookup().findVirtual(InliningCache.class, "slowPath", MethodType.methodType(int.class, String.class));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }

        private final List<String> matches;

        public InliningCache(String... matches) {
            /*super(MethodType.methodType(int.class, String.class));
            this.matches = List.of(matches);
            setTarget(MethodHandles.insertArguments(SLOW_PATH, 0, this));*/
            this(List.of(matches));
        }

        private InliningCache( List<String> matches ) {
            super(MethodType.methodType(int.class, String.class));
            this.matches = matches;
            setTarget(MethodHandles.insertArguments(SLOW_PATH, 0, this));
        }

        private int slowPath(String value) {
            var index = matches.indexOf(value);
            var mh = MethodHandles.guardWithTest(
                    MethodHandles.insertArguments(STRING_EQUALS, 1, value),
                    MethodHandles.dropArguments(MethodHandles.constant(int.class, index), 0, String.class),
                    new InliningCache(matches).dynamicInvoker()
            );
            setTarget(mh);
            return index;
        }
    }
}
