package com.umar.apps.concurrent.countdownlatch;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public record Wrkr(Integer delay, CountDownLatch countDownLatch, String name) implements Runnable {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    public Wrkr {
        Objects.requireNonNull(delay, "delay is a required parameter.");
        Objects.requireNonNull(countDownLatch, "countDownLatch is a required parameter.");
        Objects.requireNonNull(name, "name is a required parameter.");
    }

    public static Wrkr of(Integer delay, CountDownLatch countDownLatch, String name) {
        return new Wrkr(delay, countDownLatch, name);
    }

    @Override
    public void run() {
        try{
            Thread.sleep(delay);
            countDownLatch.countDown();
            System.out.println(Thread.currentThread().getName() + " has finished.");
        }catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
