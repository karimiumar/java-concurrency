package com.umar.apps.concurrent.locks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class SharedObjectWithLockTest {

    @Test
    void whenLockAcquired_thenLockedIsTrue() {
        final SharedObjectWithLock object = new SharedObjectWithLock();
        final int threadCount = 2;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        executeThreads(object, threadCount, service);
        assertThat(object.isLocked()).isTrue();
        service.shutdown();
    }

    @Test
    void whenLocked_thenQueuedThread() {
        final SharedObjectWithLock object = new SharedObjectWithLock();
        final int threadCount = 4;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        executeThreads(object, threadCount, service);
        assertThat(object.hasQueuedThreads()).isTrue();
        service.shutdown();
    }

    @Test
    void whenGetCount_thenCorrectCount() throws InterruptedException {
        final SharedObjectWithLock object = new SharedObjectWithLock();
        final int threadCount = 4;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        executeThreads(object, threadCount, service);
        Thread.sleep(500);
        assertThat(object.getCounter()).isEqualTo(4);
        service.shutdown();
    }

    private void executeThreads(SharedObjectWithLock object, int threadCount, ExecutorService service) {
        for (int i = 0; i < threadCount; i++) {
            service.execute(object::increment);
        }
    }
}
