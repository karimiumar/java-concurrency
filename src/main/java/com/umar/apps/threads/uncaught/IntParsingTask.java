package com.umar.apps.threads.uncaught;

import java.util.logging.Level;
import java.util.logging.Logger;

public class IntParsingTask implements Runnable {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private final String toParse;

    public IntParsingTask(String string) {
        toParse = string;
    }

    @Override
    public void run() {
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
            LOGGER.log(Level.SEVERE, String.format("""
            An exception occurred during IntParsingTask for Thread:%s
            Cause:%s %s
            Thread Status: %s
            """, t.getId(), e.getClass(), e.getMessage(), t.getState()
            ));
        });
        LOGGER.info("" +Integer.parseInt(toParse));
    }
}
