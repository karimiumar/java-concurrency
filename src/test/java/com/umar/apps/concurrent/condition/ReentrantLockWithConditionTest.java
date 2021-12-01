package com.umar.apps.concurrent.condition;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;

public class ReentrantLockWithConditionTest {

    @Test
    void givenReentrantLockWithCondition_whenStackEmptyThenWaitAndWhenStackFull_thenWait() {
        var service = Executors.newFixedThreadPool(2);
        var object = ReentrantLockWithCondition.of();
        service.execute(() -> {
            for (int i = 0; i < 10; i++) {
                object.push("Item " + i);
            }
        });

        service.execute(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.printf("Popped:%s \n", object.pop());
            }
        });

        service.shutdown();
    }
}
