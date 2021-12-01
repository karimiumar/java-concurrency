package com.umar.apps.concurrent.exchanger;

import java.util.Objects;
import java.util.concurrent.Exchanger;

public record Producer(Exchanger<String> exchanger) implements Runnable{

    public Producer {
        Objects.requireNonNull(exchanger, "exchanger is required");
    }

    public static Producer of(Exchanger<String> exchanger, String string) {
        return new Producer(exchanger);
    }

    @Override
    public void run() {
        String string = "";
        for (int i = 0; i < 5; i++) {
            string += i;
            System.out.printf("Produced: %s\n", string);
            try {
                string = exchanger.exchange(string);
            }catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
