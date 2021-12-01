# CountdownLatch
A synchronization aid that allows one or more threads to wait until a set of operations in other threads completes.

A `CountDownLatch` is initialized with an initial given `count`. The `await()` method blocks until the current count reaches zero due to invocations of the `countDown()` method, after which all waiting threads are released and any subsequent invocations of `await()` return immediately.
The `count` cannot be reset. Consider using [CyclicBarrier](CyclicBarrier.md) for resetting `count`.


### An example

```java
public record Worker(List<String> outputScraper, CountDownLatch countDownLatch) implements Runnable {

    public Worker {
        Objects.requireNonNull(outputScraper, "outputScraper is required.");
        Objects.requireNonNull(countDownLatch, "countDownLatch is required.");
    }

    @Override
    public void run() {
        outputScraper.add("Counted Down");
        countDownLatch.countDown();
    }
}

class CountDownLatchTest {

    private List<String> outputScraper;

    @BeforeEach
    void before() {
        outputScraper = new CopyOnWriteArrayList<>();
    }

    @Test
    void whenParallelProcessing_thenMainThreadWillBlockUntilCompletion() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);
        var workers = Stream.generate(() -> new Thread(new Worker(outputScraper, countDownLatch)))
                .limit(5).toList();
        //When
        workers.forEach(Thread::start);
        countDownLatch.await();//Block until workers finish
        outputScraper.add("Latch Released");
        assertThat(outputScraper).containsExactly("Counted Down", "Counted Down", "Counted Down", "Counted Down", "Counted Down", "Latch Released");
    }
}
```

“Latch released” will always be the last output – as it's dependant on the `CountDownLatch` releasing.

Note that if we didn't call `await()`, we wouldn't be able to guarantee the ordering of the execution of the threads, so the test would randomly fail.
