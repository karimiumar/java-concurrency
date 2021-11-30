package com.umar.apps.concurrent.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class SharedObjectWithLock {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private final ReentrantLock lock = new ReentrantLock(true);

    private int counter = 0;

    void increment() {
        lock.lock();
        LOGGER.info(String.format("Thread - %s acquired the lock.", Thread.currentThread().getName()));
        try{
            LOGGER.info(String.format("Thread - %s processing...", Thread.currentThread().getName()));
            ++counter;
        }catch (Exception exception) {
            LOGGER.log(Level.SEVERE, String.format("Interrupted Exception:%s", exception));
        }finally {
            lock.unlock();
            LOGGER.info(String.format("Thread - %s released the lock.", Thread.currentThread().getName()));
        }
    }

    void performTryLock() {
        LOGGER.info(String.format("Thread - %s attempting to acquire lock.", Thread.currentThread().getName()));
        try{
            boolean isLockAcquired = lock.tryLock(2, TimeUnit.SECONDS);
            if(isLockAcquired) {
                try {
                    LOGGER.info(String.format("Thread - %s acquired the lock.", Thread.currentThread().getName()));
                    LOGGER.info(String.format("Thread - %s processing...", Thread.currentThread().getName()));
                    sleep(1000);
                }finally {
                    lock.unlock();
                    LOGGER.info(String.format("Thread - %s released the lock.", Thread.currentThread().getName()));
                }
            }
        }catch (InterruptedException exception) {
            LOGGER.log(Level.SEVERE, String.format("Interrupted Exception:%s", exception));
        }
        LOGGER.info(String.format("Thread - %s couldn't acquire lock.", Thread.currentThread().getName()));
    }

    public ReentrantLock getLock() {
        return lock;
    }

    boolean isLocked() {
        return lock.isLocked();
    }

    boolean hasQueuedThreads() {
        return lock.hasQueuedThreads();
    }

    public int getCounter() {
        return counter;
    }
}
