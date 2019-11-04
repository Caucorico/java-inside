package fr.umlv.java.inside.lab6;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTest {

    @Test
    public void testSchedulerConstructorShouldNotAcceptNull() {
        assertThrows(NullPointerException.class, () -> new Scheduler(null));
    }

    @ParameterizedTest
    @EnumSource(Scheduler.Mode.class)
    public void testEnqueueShouldNotAcceptNull(Scheduler.Mode mode) {
        var scheduler = new Scheduler(mode);
        assertThrows(NullPointerException.class, () -> scheduler.enqueue(null));
    }

    @Test
    public void schedulerFIFOOrderOk() {
        StringBuilder sb = new StringBuilder();
        ContinuationScope scope = new ContinuationScope("test");
        Scheduler scheduler = new Scheduler(Scheduler.Mode.FIFO);
        StringBuilder genString = new StringBuilder();

        sb.append("start 1")
            .append("start 2")
            .append("middle 1")
            .append("middle 2")
            .append("end 1")
            .append("end 2");

        Continuation continuation1 = new Continuation(scope, () -> {
            genString.append("start 1");
            scheduler.enqueue(scope);
            genString.append("middle 1");
            scheduler.enqueue(scope);
            genString.append("end 1");
        });

        Continuation continuation2 = new Continuation(scope, () -> {
            genString.append("start 2");
            scheduler.enqueue(scope);
            genString.append("middle 2");
            scheduler.enqueue(scope);
            genString.append("end 2");
        });

        var list = List.of(continuation1, continuation2);
        list.forEach(Continuation::run);
        scheduler.runLoop();

        assertEquals(sb.toString(), genString.toString());
    }

    @Test
    public void schedulerSTACKOrderOk() {
        StringBuilder sb = new StringBuilder();
        ContinuationScope scope = new ContinuationScope("test");
        Scheduler scheduler = new Scheduler(Scheduler.Mode.STACK);
        StringBuilder genString = new StringBuilder();

        sb.append("start 1")
                .append("start 2")
                .append("middle 2")
                .append("end 2")
                .append("middle 1")
                .append("end 1");

        Continuation continuation1 = new Continuation(scope, () -> {
            genString.append("start 1");
            scheduler.enqueue(scope);
            genString.append("middle 1");
            scheduler.enqueue(scope);
            genString.append("end 1");
        });

        Continuation continuation2 = new Continuation(scope, () -> {
            genString.append("start 2");
            scheduler.enqueue(scope);
            genString.append("middle 2");
            scheduler.enqueue(scope);
            genString.append("end 2");
        });

        var list = List.of(continuation1, continuation2);
        list.forEach(Continuation::run);
        scheduler.runLoop();

        assertEquals(sb.toString(), genString.toString());
    }

    @Test
    public void schedulerRANDOMOk() {
        ContinuationScope scope = new ContinuationScope("test");
        Scheduler scheduler = new Scheduler(Scheduler.Mode.RANDOM);
        StringBuilder genString = new StringBuilder();

        Continuation continuation1 = new Continuation(scope, () -> {
            genString.append("start 1");
            scheduler.enqueue(scope);
            genString.append("middle 1");
            scheduler.enqueue(scope);
            genString.append("end 1");
        });

        Continuation continuation2 = new Continuation(scope, () -> {
            genString.append("start 2");
            scheduler.enqueue(scope);
            genString.append("middle 2");
            scheduler.enqueue(scope);
            genString.append("end 2");
        });

        var list = List.of(continuation1, continuation2);
        list.forEach(Continuation::run);
        scheduler.runLoop();

        var result = genString.toString();

        assertAll(
                () -> assertTrue(result.contains("start 1")),
                () -> assertTrue(result.contains("start 2")),
                () -> assertTrue(result.contains("middle 1")),
                () -> assertTrue(result.contains("middle 2")),
                () -> assertTrue(result.contains("end 1")),
                () -> assertTrue(result.contains("end 2"))
        );
    }

}
