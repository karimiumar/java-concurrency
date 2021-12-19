package com.umar.apps.concurrent.countdownlatch;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class WrkrCntDownLatchTest {

    @Test
    void givenWrkr_whenMainAwaits_thenWrkrsCntDown() throws InterruptedException {
        var latch = new CountDownLatch(4);
        var cnt = new AtomicInteger(1);
        var workers = Stream.generate(() -> new Thread(Wrkr.of(1000, latch, "WORKER-" + cnt.getAndIncrement())))
                .limit(4).toList();
        workers.forEach(Thread::start);
        latch.await();
        System.out.println(Thread.currentThread().getName() + " has finished.");
    }
}
