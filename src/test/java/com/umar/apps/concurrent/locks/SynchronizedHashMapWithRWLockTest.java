package com.umar.apps.concurrent.locks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class SynchronizedHashMapWithRWLockTest {

    @Test
    void whenWriting_thenNoReading() {
        SynchronizedHashMapWithRWLock object = new SynchronizedHashMapWithRWLock();
        final int threadCount = 3;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        executeWriterThreads(object, threadCount, service);
        assertThat(object.isReadLockAvailable()).isFalse();
        service.shutdown();
    }

    @Test
    void whenReading_thenMultipleReadingThreadsAllowed() {
        SynchronizedHashMapWithRWLock object = new SynchronizedHashMapWithRWLock();
        final int threadCount = 5;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        executeReaderThreads(object, threadCount, service);
        assertThat(object.isReadLockAvailable()).isTrue();
        service.shutdown();
    }

    private void executeWriterThreads(SynchronizedHashMapWithRWLock object, int threadCount, ExecutorService service) {
        for (int i = 0; i < threadCount; i++) {
            service.execute(() -> {
                object.put("key" + threadCount, "value" + threadCount);
            });
        }
    }

    private void executeReaderThreads(SynchronizedHashMapWithRWLock object, int threadCount, ExecutorService service) {
        for (int i = 0; i < threadCount; i++) {
            service.execute(() -> object.get("key" + threadCount));
        }
    }
}
