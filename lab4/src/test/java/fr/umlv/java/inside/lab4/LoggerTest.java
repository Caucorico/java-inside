package fr.umlv.java.inside.lab4;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {

    @Test
    public void testReturnOf() {
        A.LOGGER.log("toto");
        assertEquals("toto", A.SB.toString());
    }

    @Test
    public void testOfNullParameter() {
        assertAll(
            () -> assertThrows(NullPointerException.class, () -> Logger.of(null, e -> {})),
            () -> assertThrows(NullPointerException.class, () -> Logger.of(A.class, null))
        );
    }

    private static class A {
        private static final StringBuilder SB = new StringBuilder();
        private static final Logger LOGGER = Logger.of(Object.class, A.SB::append);
    }
}