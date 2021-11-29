package com.umar.apps.concurrent.locks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class SynchronizedHashMapWithRWLock {

    private static Map<String, String> syncHashMap = new HashMap<>();

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public void put(String key, String value) {
        try{
            writeLock.lock();
            LOGGER.info(String.format("%s writing.", Thread.currentThread().getName()));
            syncHashMap.put(key, value);
            sleep(500);
        }catch (InterruptedException exception) {
            LOGGER.log(Level.SEVERE, String.format("Exception occurred: %s", exception.getMessage()));
        } finally {
            writeLock.unlock();
        }
    }

    public String get(String key) {
        try {
            readLock.lock();
            LOGGER.info(String.format("%s reading.", Thread.currentThread().getName()));
            return syncHashMap.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public String remove(String key) {
        try {
            writeLock.lock();
            LOGGER.info(String.format("%s removing", Thread.currentThread().getName()));
            return syncHashMap.remove(key);
        }finally {
            writeLock.unlock();
        }
    }


    public boolean containsKey(String key) {
        try{
            readLock.lock();
            return syncHashMap.containsKey(key);
        }finally {
            readLock.unlock();
        }
    }

    boolean isReadLockAvailable() {
        return readLock.tryLock();
    }

    private static class Reader implements Runnable {

        SynchronizedHashMapWithRWLock object;

        Reader(SynchronizedHashMapWithRWLock object) {
            Objects.requireNonNull(object);
            this.object = object;
        }

        public void run() {
            for (int i = 0; i < 10; i++) {
                object.get("key" + i);
            }
        }
    }

    private static class Writer implements Runnable {

        SynchronizedHashMapWithRWLock object;

        Writer(SynchronizedHashMapWithRWLock object) {
            this.object = object;
        }

        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    object.put("key" + i, "value" + i);
                    sleep(500);
                }catch (InterruptedException exception) {
                    LOGGER.log(Level.SEVERE, exception.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        final int threadCount = 3;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        SynchronizedHashMapWithRWLock object = new SynchronizedHashMapWithRWLock();

        service.execute(new Thread(new Writer(object), "Writer"));
        service.execute(new Thread(new Reader(object), "Reader1"));
        service.execute(new Thread(new Reader(object), "Reader2"));

        service.shutdown();
    }
}
