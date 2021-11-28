package com.umar.apps.threads.uncaught;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
