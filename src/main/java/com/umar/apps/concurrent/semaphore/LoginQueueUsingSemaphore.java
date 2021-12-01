package com.umar.apps.concurrent.semaphore;

import java.util.Objects;
import java.util.concurrent.Semaphore;

public record LoginQueueUsingSemaphore(Integer slotLimit, Semaphore semaphore) {

    public LoginQueueUsingSemaphore {
        Objects.requireNonNull(slotLimit, "slotLimit is required");
    }

    public static LoginQueueUsingSemaphore of(Integer slotLimit) {
        return new LoginQueueUsingSemaphore(slotLimit, new Semaphore(slotLimit));
    }

    boolean tryLogin() {
        return semaphore.tryAcquire();
    }

    void logout() {
        semaphore.release();
    }

    int availablePermits() {
        return semaphore.availablePermits();
    }
}
