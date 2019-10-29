package fr.umlv.java.inside.lab6;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Scheduler {

    private final ArrayDeque<Continuation> queue = new ArrayDeque<>();

    private final Mode mode;

    public Scheduler(Mode mode) {
        this.mode = mode;
    }

    private void add(Continuation continuation) {
        switch ( mode ) {
            case FIFO:
                queue.offer(continuation);
                break;
            case STACK:
                queue.push(continuation);
                break;
            case RANDOM:
                /* Whatever the place, random don't take care */
                queue.offer(continuation);
                break;
            default:
                throw new IllegalArgumentException("WTF ?");
        }
    }

    private Continuation remove() {
        switch ( mode ) {
            case FIFO:
                queue.pollFirst();
                break;
            case STACK:
                queue.pop();
                break;
            case RANDOM:
                ThreadLocalRandom random = ThreadLocalRandom.current();
                var buffArray = new ArrayList<>(queue);
                var buff = buffArray.remove(random.nextInt(queue.size()));
                queue = new ArrayDeque<>(buffArray);
                break;
            default:
                throw new IllegalArgumentException("WTF ?");
        }
    }

    public void enqueue(ContinuationScope scope) {
        var currentContinuation = Continuation.getCurrentContinuation(scope);
        queue.offer(currentContinuation);
        Continuation.yield(scope);
    }

    public void runLoop() {
        while ( !queue.isEmpty() ) {
            var v = queue.pollFirst();
            v.run();
        }
    }

    public static void main(String[] args) {
        var scope = new ContinuationScope("scope");
        var scheduler = new Scheduler();
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
