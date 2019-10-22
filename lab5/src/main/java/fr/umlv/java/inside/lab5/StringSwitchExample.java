package fr.umlv.java.inside.lab5;

import java.util.Objects;

public class StringSwitchExample {
    public static int testStringSwitch(String s) {
        Objects.requireNonNull(s);
        return switch (s) {
            case "foo" -> 0;
            case "bar" -> 1;
            case "bazz" -> 2;
            default -> -1;
        };
    }
}
