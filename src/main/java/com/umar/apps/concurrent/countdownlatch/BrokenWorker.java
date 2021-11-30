package com.umar.apps.concurrent.countdownlatch;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public record BrokenWorker(List<String> outputScraper, CountDownLatch countDownLatch) implements Runnable {

    public BrokenWorker {
        Objects.requireNonNull(outputScraper, "outputScraper is required.");
        Objects.requireNonNull(countDownLatch, "countDownLatch is required.");
    }

    @Override
    public void run() {
        if(true) {
            throw new RuntimeException("Exception...");
        }
        countDownLatch.countDown();
        outputScraper.add("Counted down.");
    }
}
