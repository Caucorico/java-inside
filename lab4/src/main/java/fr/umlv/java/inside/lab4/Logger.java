package fr.umlv.java.inside.lab4;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Objects;
import java.util.function.Consumer;

import static java.lang.invoke.MethodHandles.insertArguments;

@FunctionalInterface
public interface Logger {
    public void log(String message);

    public static Logger of(Class<?> declaringClass, Consumer<? super String> consumer) {
        Objects.requireNonNull(declaringClass);
        Objects.requireNonNull(consumer);
        var mh = createLoggingMethodHandle(declaringClass, consumer);
        return new Logger() {
            @Override
            public void log(String message) {
                Objects.requireNonNull(message);
                try {
                    mh.invokeExact(message);
                } catch(Throwable t) {
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    }
                    if (t instanceof Error) {
                        throw (Error)t;
                    }
                    throw new UndeclaredThrowableException(t);
                }
            }
        };
    }

    public static Logger fastOf(Class<?> declaringClass, Consumer<? super String> consumer) {
        Objects.requireNonNull(declaringClass);
        Objects.requireNonNull(consumer);
        var mh = createLoggingMethodHandle(declaringClass, consumer);
        return (message) -> {
            Objects.requireNonNull(message);
            try {
                mh.invokeExact(message);
            } catch(Throwable t) {
                if (t instanceof RuntimeException) {
                    throw (RuntimeException)t;
                }
                if (t instanceof Error) {
                    throw (Error)t;
                }
                throw new UndeclaredThrowableException(t);
            }
        };
    }

    private static MethodHandle createLoggingMethodHandle(Class<?> declaringClass, Consumer<? super String> consumer) {
        var lookup = MethodHandles.lookup();
        var methodType = MethodType.methodType(void.class, Object.class);
        MethodHandle mh;
        try {
            mh = lookup.findVirtual(Consumer.class, "accept", methodType);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new AssertionError(e);
        }

        mh = insertArguments(mh, 0, consumer);
        mh = mh.asType(MethodType.methodType(void.class, String.class));
        return mh;
    }
}
