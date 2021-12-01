package com.umar.apps.concurrent.exchanger;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;

public class ProducerConsumerExchangerTest {

    @Test
    void givenExchanger_whenProducerConsumer_thenProducesAndConsumes() {
        var service = Executors.newFixedThreadPool(2);
        var exchanger = new Exchanger<String>();
        var producerThread = new Thread(new Producer(exchanger));
        var consumerThread = new Thread(new Consumer(exchanger));
        service.submit(producerThread);
        service.submit(consumerThread);
        service.shutdown();
    }
}
