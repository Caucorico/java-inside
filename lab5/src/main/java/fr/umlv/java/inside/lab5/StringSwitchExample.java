package fr.umlv.java.inside.lab5;

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

    public static int stringSwitch2(String s) {
        throw new UnsupportedOperationException("NYI");
    }
}
