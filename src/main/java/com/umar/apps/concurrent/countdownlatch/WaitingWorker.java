package com.umar.apps.concurrent.countdownlatch;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public record WaitingWorker(
        List<String> outputScraper
        ,CountDownLatch readyThreadCounter
        ,CountDownLatch callingThreadBlocker
        ,CountDownLatch completedThreadCounter) implements Runnable{

    public WaitingWorker {
        Objects.requireNonNull(outputScraper, "outputScraper is required");
        Objects.requireNonNull(readyThreadCounter, "readyThreadCounter is required");
        Objects.requireNonNull(callingThreadBlocker, "callingThreadBlocker is required");
        Objects.requireNonNull(completedThreadCounter, "completedThreadCounter is required");
    }

    @Override
    public void run() {
        readyThreadCounter.countDown();
        try{
            callingThreadBlocker.await();
            outputScraper.add("Counted down");
        }catch (InterruptedException exception) {
            exception.printStackTrace();
        }finally {
            completedThreadCounter.countDown();
        }
    }
}
