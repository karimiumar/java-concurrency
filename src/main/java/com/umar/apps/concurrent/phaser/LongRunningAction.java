package com.umar.apps.concurrent.phaser;

import java.util.Objects;
import java.util.concurrent.Phaser;

import static java.lang.Thread.sleep;

public record LongRunningAction(String threadName, Phaser phaser) implements Runnable {

    public LongRunningAction {
        Objects.requireNonNull(threadName, "threadName is required");
        Objects.requireNonNull(phaser, "phaser is required");
        phaser.register();
    }

    public static LongRunningAction of(String threadName, Phaser phaser) {
        return new LongRunningAction(threadName, phaser);
    }

    @Override
    public void run() {
        System.out.printf("This is phase %d\n", phaser.getPhase());
        System.out.printf("Thread - %s before long running action\n", threadName);
        phaser.arriveAndAwaitAdvance();
        try{
            sleep(20);
        }catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        phaser.arriveAndDeregister();
    }
}
