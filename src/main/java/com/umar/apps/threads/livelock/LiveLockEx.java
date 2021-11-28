package com.umar.apps.threads.livelock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LiveLockEx {

    private static final Lock lock1 = new ReentrantLock(true);
    private static final Lock lock2 = new ReentrantLock(true);

    public static void main(String[] args) {
        var liveLock = new LiveLockEx();
        new Thread(liveLock:: operation1, "T1").start();
        new Thread(liveLock:: operation2, "T2").start();
    }

    void operation1() {
        while (true) {
            tryLock(lock1, 50);
            print("Lock1 acquired. Trying to acquire Lock2");
            sleep(50);

            if(tryLock(lock2)) {
                print("Lock2 acquired");
            } else {
                print("Cannot acquire Lock2, releasing Lock1");
                lock1.unlock();
                continue;
            }
            print("Executing operation1()");
            break;
        }
    }

    void operation2() {
        while (true) {
            tryLock(lock2, 50);
            print("Lock2 acquired. Trying to acquire Lock1");
            sleep(50);
            if(tryLock(lock1)) {
                print("Lock1 acquired.");
            }else {
                print("Cannot acquire lock1. Releasing  lock1");
                lock1.unlock();
                continue;
            }
            print("executing operation2()");
            break;
        }

        lock1.unlock();
        lock2.unlock();
    }

    private void tryLock(Lock lock, long millis) {
        try {
            lock.tryLock(millis, TimeUnit.MILLISECONDS);
        }catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private boolean tryLock(Lock lock) {
        return lock.tryLock();
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
