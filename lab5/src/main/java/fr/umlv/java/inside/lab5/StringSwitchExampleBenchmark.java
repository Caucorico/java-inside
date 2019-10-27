package fr.umlv.java.inside.lab5;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class StringSwitchExampleBenchmark {

    private static final int INSERTION_NUMBER = 1_000_000;

    private static final String[] words = { "foo", "bar", "bazz", "a", "aa", "aaa"};

    private static final ArrayList<String> testList = new ArrayList<>();

    static {
        var random = new Random();

        for ( var i = 0 ; i < INSERTION_NUMBER ; i++ ) {
            testList.add(words[random.nextInt(words.length)]);
        }
    }

    @Benchmark
    public void stringSwitch1() {
        testList.forEach(StringSwitchExample::stringSwitch);
    }

    @Benchmark
    public void stringSwitch2() {
        testList.forEach(StringSwitchExample::stringSwitch2);
    }

    @Benchmark
    public void stringSwitch3() {
        testList.forEach(StringSwitchExample::stringSwitch3);
    }
}
