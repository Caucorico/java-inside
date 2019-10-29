package fr.umlv.java.inside.lab6;

import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

public class Example1 {
    public static void main(String[] args) {
        var scope = new ContinuationScope("scope");
        var continuation1 = new Continuation(scope, () -> {
            System.out.println("start 1");
            Continuation.yield(scope);
            System.out.println("end 1");
        });
        var continuation2 = new Continuation(scope, () -> {
            System.out.println("start 2");
            Continuation.yield(scope);
            System.out.println("end 2");
        });
        var list = List.of(continuation1, continuation2);

        /* Methode 1 :*/
        /*var buff = List.copyOf(list);
        while ( !buff.isEmpty() ) {
            buff = buff.stream()
                .map( e -> {
                    e.run();
                    return e;
                })
                .filter( e -> !e.isDone() )
                .collect(Collectors.toList());
        }*/

        /* Methode 2 : */
        var queue = new ArrayDeque<Continuation>(list);
        while ( !queue.isEmpty() ) {
            var v = queue.pollFirst();
            v.run();
            if ( !v.isDone() ) queue.offerLast(v);
        }
    }
}
