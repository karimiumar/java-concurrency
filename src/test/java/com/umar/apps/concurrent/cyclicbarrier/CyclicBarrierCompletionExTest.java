package com.umar.apps.concurrent.cyclicbarrier;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CyclicBarrierCompletionExTest {

    @Test
    void givenCyclicBarrier_whenPartiesIs7WithThreadCount20_thenCountTripsIs2() {
        //Whenever the threadCount reaches 7, CyclicBarrier increments the AtomicCount
        //that is, it divides the operation into 3 buckets of 7 parties in each
        //So when
        //threadCount : 0 - 6, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 0
        //threadCount : 7 - 13, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 1
        //threadCount : 14 - 20, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 2
        var ex = CyclicBarrierCompletionEx.of(7, 20);
        assertThat(ex.countTrips()).isEqualTo(2);
    }

    @Test
    void givenCyclicBarrier_whenPartiesIs5WithThreadCount20_thenCountTripsIs4() {
        //Whenever the threadCount reaches 5, CyclicBarrier increments the Atomic Count
        //that is, it divides the operation into 4 buckets of 5 parties in each
        //So when
        //threadCount : 0 - 4, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 0
        //threadCount : 5 - 9, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 1
        //threadCount : 10 - 14, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 2
        //threadCount : 15 - 19, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 3
        //threadCount : 20, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 4
        var ex = CyclicBarrierCompletionEx.of(5, 20);
        assertThat(ex.countTrips()).isEqualTo(4);
    }
}
