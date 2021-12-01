package com.umar.apps.concurrent.exchanger;

import java.util.Objects;
import java.util.concurrent.Exchanger;

public record Consumer(Exchanger<String> exchanger) implements Runnable {

    public Consumer {
        Objects.requireNonNull(exchanger, "exchanger is required.");
    }

    public static Consumer of(Exchanger<String> exchanger) {
        return new Consumer(exchanger);
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try{
                var value = exchanger.exchange("" + i);
                System.out.printf("Consumed:%s\n", value);
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
