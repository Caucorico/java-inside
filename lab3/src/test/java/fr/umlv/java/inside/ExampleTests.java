package fr.umlv.java.inside;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExampleTests {

    @Test
    public void testCallAStaticHello() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var method = Example.class.getDeclaredMethod("aStaticHello", int.class);
        method.setAccessible(true);
        assertEquals("question"+" 3", method.invoke(null, 3));
    }

    @Test
    public void testCallAStaticHelloWithLookup() throws Throwable {
        var lookup = MethodHandles.privateLookupIn(Example.class, MethodHandles.lookup());
        assertEquals("question"+" 4", (String) lookup.findStatic(Example.class, "aStaticHello", MethodType.methodType(String.class, int.class)).invokeExact( 4));
    }

    @Test
    public void testCallAnInstanceHello() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var method = Example.class.getDeclaredMethod("anInstanceHello", int.class);
        method.setAccessible(true);
        var ex = new Example();;
        assertEquals("question"+" 3", method.invoke(ex, 3));
    }

    @Test
    public void testCallAnInstanceHelloWithLookup() throws Throwable {
        var lookup = MethodHandles.privateLookupIn(Example.class, MethodHandles.lookup());
        var methodHandle = lookup.findVirtual(Example.class, "anInstanceHello", MethodType.methodType(String.class, int.class));
        methodHandle = MethodHandles.insertArguments(methodHandle, 0, new Example());
        assertEquals("question"+" 5", (String) methodHandle.invokeExact(5));
    }

    @Test
    public void testCallAnInstanceHelloWithLookupAnd8Argument() throws Throwable {
        var lookup = MethodHandles.privateLookupIn(Example.class, MethodHandles.lookup());
        var methodHandle = lookup.findVirtual(Example.class, "anInstanceHello", MethodType.methodType(String.class, int.class));
        methodHandle = MethodHandles.insertArguments(methodHandle, 1, 8);
        assertEquals("question"+" 8", (String) methodHandle.invokeExact(new Example()));
    }

    @Test
    public void testCallAnInstanceHelloWithDropArguments() throws Throwable {
        var lookup = MethodHandles.privateLookupIn(Example.class, MethodHandles.lookup());
        var methodHandle = lookup.findVirtual(Example.class, "anInstanceHello", MethodType.methodType(String.class, int.class));
        methodHandle = MethodHandles.insertArguments(methodHandle, 1, 8);
        methodHandle = MethodHandles.dropArguments(methodHandle, 1, int.class );
        assertEquals("question"+" 8", (String) methodHandle.invokeExact(new Example(), 8));
    }


    @Test
    public void testCallAnInstanceHelloWithUnboxing() throws Throwable {
        var lookup = MethodHandles.privateLookupIn(Example.class, MethodHandles.lookup());
        var methodHandle = lookup.findVirtual(Example.class, "anInstanceHello", MethodType.methodType(String.class, int.class));
        methodHandle = methodHandle.asType(MethodType.methodType(String.class, Example.class, Integer.class));
        assertEquals("question"+" 8", (String) methodHandle.invokeExact(new Example(), Integer.valueOf(8)));
    }

    @Test
    public void testCallOfConstantMethodHandles() throws Throwable {
        var cst = MethodHandles.constant(Integer.class, 9);
        assertEquals(9, (Integer)cst.invokeExact());
    }

    @Test
    public void testCallAnInstanceHelloWithGuard() throws Throwable {
        var lookup = MethodHandles.lookup();
        var methodHandle = lookup.findVirtual(String.class, "equals", MethodType.methodType(boolean.class, Object.class));
        var cst1 = MethodHandles.constant(int.class, 1);
        var cstm1 = MethodHandles.constant(int.class, -1);

        cst1 = MethodHandles.dropArguments(cst1, 0, String.class, Object.class);
        cstm1 = MethodHandles.dropArguments(cstm1, 0, String.class, Object.class);
        var guard = methodHandle.asType(MethodType.methodType(boolean.class, String.class, Object.class));
        guard = MethodHandles.guardWithTest(guard, cst1, cstm1);
        guard = MethodHandles.insertArguments(guard, 1, "foo");

        assertEquals(1, (int) guard.invokeExact("foo"));
    }
}
