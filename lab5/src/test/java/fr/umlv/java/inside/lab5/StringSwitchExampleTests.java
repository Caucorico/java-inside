package fr.umlv.java.inside.lab5;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringSwitchExampleTests {

    @Test
    public void testStringSwitch() {
        assertAll(
            () -> assertTrue(true),
            () -> assertEquals(0, StringSwitchExample.testStringSwitch("foo")),
            () -> assertEquals(1, StringSwitchExample.testStringSwitch("bar")),
            () -> assertEquals(2, StringSwitchExample.testStringSwitch("bazz")),
            () -> assertEquals(-1, StringSwitchExample.testStringSwitch("a")),
            () -> assertEquals(-1, StringSwitchExample.testStringSwitch("aa")),
            () -> assertEquals(-1, StringSwitchExample.testStringSwitch("aaa")),
            () -> assertEquals(-1, StringSwitchExample.testStringSwitch("aaaa")),
            () -> assertEquals(-1, StringSwitchExample.testStringSwitch("aaaaa")),
            () -> assertThrows(NullPointerException.class, () -> StringSwitchExample.testStringSwitch(null) )
        );
    }
}
