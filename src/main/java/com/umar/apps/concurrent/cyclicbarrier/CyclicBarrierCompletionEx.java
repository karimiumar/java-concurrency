package com.umar.apps.concurrent.cyclicbarrier;

import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public record CyclicBarrierCompletionEx(Integer parties, Integer threadCount, AtomicInteger updateCount) {

    public CyclicBarrierCompletionEx {
        Objects.requireNonNull(parties, "parties is required");
        Objects.requireNonNull(threadCount, "threadCount is required");
    }

    public static CyclicBarrierCompletionEx of(Integer parties, Integer threadCount) {
        return new CyclicBarrierCompletionEx(parties, threadCount, new AtomicInteger(0));
    }

    public int countTrips() {
        var cyclicBarrier = new CyclicBarrier(parties, updateCount::incrementAndGet);
        var es = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            es.execute(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        es.shutdown();

        try {
            es.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return updateCount.get();
    }
}
