package com.umar.apps.concurrent.threadlocal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ThreadLocalIntegrationTest {

    @Test
    void givenThreadThatStoresContextInAMap_whenStartThread_thenShouldSetContextForBothUsers() throws InterruptedException {
        var user1 = new SharedMapWithUserContext(1);
        var user2 = new SharedMapWithUserContext(2);
        new Thread(user1).start();
        new Thread(user2).start();
        Thread.sleep(3000);
        assertThat(SharedMapWithUserContext.userContextPerUserId.size()).isEqualTo(2);
    }

    @Test
    void givenThreadThatStoresContextInThreadLocal_whenStartThread_thenShouldSetContextForAllUsersAndLogMessages() throws InterruptedException {
        var user1 = new ThreadLocalWithUserContext(1);
        var user2 = new ThreadLocalWithUserContext(2);
        var user3 = new ThreadLocalWithUserContext(3);
        var user4 = new ThreadLocalWithUserContext(4);
        var user5 = new ThreadLocalWithUserContext(5);
        var user6 = new ThreadLocalWithUserContext(6);
        new Thread(user1).start();
        new Thread(user2).start();
        new Thread(user3).start();
        new Thread(user4).start();
        new Thread(user5).start();
        new Thread(user6).start();

        Thread.sleep(3000);
    }
}
