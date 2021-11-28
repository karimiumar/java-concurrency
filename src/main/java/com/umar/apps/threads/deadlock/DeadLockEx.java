package com.umar.apps.threads.deadlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadLockEx {

    private static final Lock lock1 = new ReentrantLock(true);
    private static final Lock lock2 = new ReentrantLock(true);

    public static void main(String[] args) {
        DeadLockEx deadLockEx = new DeadLockEx();
        new Thread(deadLockEx::operation1, "T1").start();
        new Thread(deadLockEx::operation2, "T2").start();
    }

    void operation1() {
        lock1.lock();
        print("Lock1 acquired, waiting to acquire lock2");
        sleep(50);
        lock2.lock();
        print("lock2 acquired");
        print("Executing operation1()");
        lock2.unlock();
        lock1.unlock();
    }

    void operation2() {
        lock2.lock();
        print("Lock2 acquired, waiting to acquire lock1");
        sleep(50);
        lock1.lock();
        print("Acquired lock1");
        print("Executing operation2");
        lock1.unlock();
        lock2.unlock();
    }

    void print(String message) {
        System.out.printf(" Thread: %s: message:%s\n", Thread.currentThread().getName(), message);
    }

    void sleep(long millis) {
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
