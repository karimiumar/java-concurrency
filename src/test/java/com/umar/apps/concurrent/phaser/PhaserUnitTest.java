package com.umar.apps.concurrent.phaser;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import static org.assertj.core.api.Assertions.assertThat;

public class PhaserUnitTest {

    @Test
    void givenPhaser_whnCoordinateWorksBetweenThreads_thenShouldCoordinateBetweenMultiplePhases() {
        //given
        ExecutorService service = Executors.newCachedThreadPool();
        var ph = new Phaser(1);
        assertThat(ph.getPhase()).isEqualTo(0);

        //When..
        service.submit(LongRunningAction.of("thread-1", ph));
        service.submit(LongRunningAction.of("thread-2", ph));
        service.submit(LongRunningAction.of("thread-3", ph));

        //then..
        ph.arriveAndAwaitAdvance();
        assertThat(ph.getPhase()).isEqualTo(1);

        //and
        service.submit(LongRunningAction.of("thread-4", ph));
        service.submit(LongRunningAction.of("thread-5", ph));
        ph.arriveAndAwaitAdvance();
        assertThat(ph.getPhase()).isEqualTo(2);

        //ph.arriveAndDeregister();
    }
}
