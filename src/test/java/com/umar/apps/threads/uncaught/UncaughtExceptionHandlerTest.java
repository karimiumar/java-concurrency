package com.umar.apps.threads.uncaught;

import org.junit.jupiter.api.Test;

public class UncaughtExceptionHandlerTest {

    @Test
    void givenIntParsingTask_whenParseHasInvalidEntry_thenLogsException() {
        var intParsingTask = new IntParsingTask("abc");
        new Thread(intParsingTask).start();
    }

    @Test
    void givenIntParsingTask_whenParseIsValid_thenLogsIntValue() {
        var intParsingTask = new IntParsingTask("123");
        new Thread(intParsingTask).start();
    }
}
