package fr.umlv.java.inside.lab6;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Scheduler {

    private final Collection<Continuation> queue;

    private final Mode mode;

    public Scheduler(Mode mode) {
        this.mode = Objects.requireNonNull(mode);
        if ( mode == Mode.RANDOM ) {
            queue = new ArrayList<>();
        } else {
            queue = new ArrayDeque<>();
        }
    }

    private void add(Continuation continuation) {
        switch ( mode ) {
            case FIFO:
                ((ArrayDeque<Continuation>) queue).offer(continuation);
                break;
            case STACK:
                ((ArrayDeque<Continuation>) queue).push(continuation);
                break;
            case RANDOM:
                /* Whatever the place, random don't take care */
                queue.add(continuation);
                break;
            default:
                throw new IllegalArgumentException("WTF ?");
        }
    }

    private Continuation remove() {
        switch ( mode ) {
            case FIFO:
                return ((ArrayDeque<Continuation>) queue).pollFirst();
            case STACK:
                return ((ArrayDeque<Continuation>) queue).pop();
            case RANDOM:
                ThreadLocalRandom random = ThreadLocalRandom.current();
                return ((ArrayList<Continuation>) queue).remove(random.nextInt(queue.size()));
            default:
                throw new IllegalArgumentException("WTF ?");
        }
    }

    public void enqueue(ContinuationScope scope) {
        Objects.requireNonNull(scope);
        var currentContinuation = Continuation.getCurrentContinuation(scope);
        this.add(currentContinuation);
        Continuation.yield(scope);
    }

    public void runLoop() {
        while ( !queue.isEmpty() ) {
            var v = this.remove();
            v.run();
        }
    }

    public static void main(String[] args) {
        var scope = new ContinuationScope("scope");
        var scheduler = new Scheduler(Mode.STACK);
        var continuation1 = new Continuation(scope, () -> {
            System.out.println("start 1");
            scheduler.enqueue(scope);
            System.out.println("middle 1");
            scheduler.enqueue(scope);
            System.out.println("end 1");
        });
        var continuation2 = new Continuation(scope, () -> {
            System.out.println("start 2");
            scheduler.enqueue(scope);
            System.out.println("middle 2");
            scheduler.enqueue(scope);
            System.out.println("end 2");
        });
        var list = List.of(continuation1, continuation2);
        list.forEach(Continuation::run);
        scheduler.runLoop();

    }

    public enum Mode {
        STACK, FIFO, RANDOM
    }
}
