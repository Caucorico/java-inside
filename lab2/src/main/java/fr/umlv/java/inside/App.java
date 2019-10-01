package fr.umlv.java.inside;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
       System.out.println(JsonPrinter.toJson(new Person("test", "test")));
        System.out.println(JsonPrinter.toJson(new Alien("sdfvrd", 12)));
    }
}
