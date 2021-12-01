package com.umar.apps.concurrent.countdownlatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CountDownLatchTest {

    private List<String> outputScraper;

    @BeforeEach
    void before() {
        outputScraper = new CopyOnWriteArrayList<>();
    }

    @Test
    void whenParallelProcessing_thenMainThreadWillBlockUntilCompletion() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        var workers = Stream.generate(() -> new Thread(new Worker(outputScraper, countDownLatch)))
                .limit(5).toList();
        //When
        workers.forEach(Thread::start);
        countDownLatch.await();//Block until workers finish
        outputScraper.add("Latch Released");
        assertThat(outputScraper).containsExactly("Counted Down","Counted Down","Counted Down","Counted Down","Counted Down","Latch Released");
    }

    @Test
    void whenFailingToProcessParallely_thenMainThreadShouldTimeout() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        var workers = Stream.generate(() -> new Thread(new BrokenWorker(outputScraper, countDownLatch)))
                .limit(5).toList();

        //When..
        workers.forEach(Thread::start);

        final boolean result = countDownLatch.await(3L, TimeUnit.SECONDS);

        //Then
        assertThat(result).isFalse();
    }

    @Test
    void whenProcessingThingsParallely_thenStartAllTheProcessesAtSameTime() throws InterruptedException {
        CountDownLatch readyThreadCounter = new CountDownLatch(5);
        CountDownLatch callingThreadBlocker = new CountDownLatch(1);
        CountDownLatch completedThreadCounter = new CountDownLatch(5);
        var workers = Stream.generate(() -> new Thread(
                new WaitingWorker(outputScraper, readyThreadCounter, callingThreadBlocker, completedThreadCounter)
        )).limit(5).toList();

        //When
        workers.forEach(Thread::start);
        readyThreadCounter.await();
        outputScraper.add("Workers Ready");
        callingThreadBlocker.countDown();
        completedThreadCounter.await();
        outputScraper.add("Workers complete");

        //Then
        assertThat(outputScraper).containsExactly("Workers Ready", "Counted down","Counted down","Counted down","Counted down","Counted down", "Workers complete");
    }
}
