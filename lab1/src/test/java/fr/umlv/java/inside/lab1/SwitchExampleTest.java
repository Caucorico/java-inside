package fr.umlv.java.inside.lab1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

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
}