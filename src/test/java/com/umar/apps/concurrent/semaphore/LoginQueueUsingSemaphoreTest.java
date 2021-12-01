package com.umar.apps.concurrent.semaphore;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginQueueUsingSemaphoreTest {

    @Test
    void givenLoginQueue_whenFailedLoginLimitReached_thenBlocked(){
        int slots = 5;
        var service = Executors.newFixedThreadPool(slots);
        var loginQueue = LoginQueueUsingSemaphore.of(slots);
        IntStream.range(0, slots).forEach(user -> service.execute(loginQueue::tryLogin));
        service.shutdown();
        assertThat(loginQueue.availablePermits()).isEqualTo(0);
        assertThat(loginQueue.tryLogin()).isFalse();
    }

    @Test
    void givenLoginQueue_whenLogout_thenSlotsAvailable() {
        int slot = 5;
        var service = Executors.newFixedThreadPool(slot);
        var loginQueue = LoginQueueUsingSemaphore.of(slot);
        IntStream.range(0, slot).forEach(user -> service.execute(loginQueue::tryLogin));
        service.shutdown();
        loginQueue.logout();
        assertThat(loginQueue.availablePermits()).isEqualTo(0);
        assertThat(loginQueue.tryLogin()).isTrue();
    }
 }
