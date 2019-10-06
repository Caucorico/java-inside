package fr.umlv.java.inside;

import java.util.Objects;

public class Main {


    public static void main(String[] args) {
        System.out.println(JsonPrinter.toJson(new Person("test", "test")));
    }


    static class Alien {
        private final String planet;
        private final int age;

        public Alien(String planet, int age) {
            if (age <= 0) {
                throw new IllegalArgumentException("Too young...");
            }
            this.planet = Objects.requireNonNull(planet);
            this.age = age;
        }

        @JSONProperty("h-e-y")
        public String getPlanet() {
            return planet;
        }

        @JSONProperty
        public int getAge() {
            return age;
        }
    }

    static class Person {
        private final String firstName;
        private final String lastName;

        public Person(String firstName, String lastName) {
            this.firstName = Objects.requireNonNull(firstName);
            this.lastName = Objects.requireNonNull(lastName);
        }

        @JSONProperty("h-e-y")
        public String getFirstName() {
            return firstName;
        }

        @JSONProperty
        public String getLastName() {
            return lastName;
        }
    }
}
