package com.umar.apps.concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {

    private final Lock lock = new ReentrantLock();
    private int value;

    public int increment() {
        lock.lock();
        try{
            return ++value;
        }finally {
            lock.unlock();
        }
    }

    public int current() {
        lock.lock();
        try{
            return value;
        }finally {
            lock.unlock();
        }
    }
}
