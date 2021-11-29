package com.umar.apps.concurrent.locks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReentrantLockTest {

    private Counter counter;
    private ExecutorService executorService;
    private int value;

    @BeforeEach
    void before() {
        counter = new Counter();
        executorService = Executors.newFixedThreadPool(4);
    }

    @Test
    void givenCounter_whenMultipleThreads_thenCorrectValueReturns() throws InterruptedException {
        Runnable incrementer = () -> {
            counter.increment();
        };

        Runnable reader1 = () -> {
            value = counter.current();
            System.out.printf("%s value:%d\n", Thread.currentThread().getName(), value);
        };

        Runnable reader2 = () -> {
            value = counter.current();
            System.out.printf("%s value:%d\n", Thread.currentThread().getName(), value);
        };

        Runnable reader3 = () -> {
            value = counter.current();
            System.out.printf("%s value:%d\n", Thread.currentThread().getName(), value);
        };

        executorService.submit(incrementer);
        executorService.submit(reader1);
        executorService.submit(reader2);
        executorService.submit(reader3);
        executorService.submit(incrementer);
        executorService.submit(reader1);
        executorService.submit(reader2);
        executorService.submit(reader3);
        executorService.submit(incrementer);
        executorService.submit(reader1);
        executorService.submit(reader2);
        executorService.submit(reader3);
        executorService.shutdown();
        Thread.sleep(500);
        assertThat(value).isEqualTo(3);
    }
}
