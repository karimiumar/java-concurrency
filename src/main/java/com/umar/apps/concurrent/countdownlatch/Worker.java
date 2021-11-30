package com.umar.apps.concurrent.countdownlatch;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public record Worker(List<String> outputScraper, CountDownLatch countDownLatch) implements Runnable {

    public Worker {
        Objects.requireNonNull(outputScraper, "outputScraper is required.");
        Objects.requireNonNull(countDownLatch, "countDownLatch is required.");
    }

    @Override
    public void run() {
        outputScraper.add("Counted Down");
        countDownLatch.countDown();
    }
}
