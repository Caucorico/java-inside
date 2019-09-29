package fr.umlv.java.inside.lab1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SwitchExampleTest {

    @Test
    public void testDog() {
        assertEquals(1, SwitchExample.switchExample("dog"));
    }

    @Test
    public void testCat() {
        assertEquals(2, SwitchExample.switchExample("cat"));
    }

    @Test
    public void testDefault() {
        assertEquals(4, SwitchExample.switchExample("default"));
    }

    @Test
    public void testFailed() {
        fail();
    }
}