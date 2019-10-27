package fr.umlv.java.inside.lab5;

import java.lang.invoke.MethodHandles;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public class StringSwitchExampleTests {

    @ParameterizedTest
    @MethodSource("methodsProvider")
    public void testStringSwitch(ToIntFunction<String> candidate) {
        assertAll(
            () -> assertTrue(true),
            () -> assertEquals(0, candidate.applyAsInt("foo")),
            () -> assertEquals(1, candidate.applyAsInt("bar")),
            () -> assertEquals(2, candidate.applyAsInt("bazz")),
            () -> assertEquals(-1, candidate.applyAsInt("a")),
            () -> assertEquals(-1, candidate.applyAsInt("aa")),
            () -> assertEquals(-1, candidate.applyAsInt("aaa")),
            () -> assertEquals(-1, candidate.applyAsInt("aaaa")),
            () -> assertEquals(-1, candidate.applyAsInt("aaaaa")),
            () -> assertThrows(NullPointerException.class, () -> candidate.applyAsInt(null) )
        );
    }

    public static Stream<ToIntFunction<String>> methodsProvider() {
        return Stream.of(
                StringSwitchExample::stringSwitch,
                StringSwitchExample::stringSwitch2,
                StringSwitchExample::stringSwitch3
        );
    }
}
