# CyclicBarrier

A `CyclicBarrier` is a synchronizer that allows a set of threads to wait for each other to reach a common point before continuing execution.

The barrier is called 'cyclic' because it can be re-used after the waiting threads are released.

## Usage

1. The constructor `public CyclicBarrier(int parties)` of CyclicBarrier takes an integer argument denoting number of threads that would call the `await()`  method on the barrier. The threads are called parties to the `CyclicBarrier`. This call is synchronous and the thread calling this method suspends execution till a specified number of threads have reached the same method in the barrier. This situation where a specified number of threads have called `await()` is called **tripping the barrier**.
2. The other constructor `public CyclicBarrier(int parties, Runnable barrierAction` allows us to pass a logic that would be run by the last thread that **trips the barrier**

## Example

In the example given below, `CyclicBarrierCompletionEx.of(Interger parties, int threadCount)`initializes an `AtomicInteger updateCount`.  This `updateCount` variable is incremented by the last thread that **trips the barrier** in the `int countTrips()` method.  
```java
public record CyclicBarrierCompletionEx(Integer parties, Integer threadCount, AtomicInteger updateCount) {

    public CyclicBarrierCompletionEx {
        Objects.requireNonNull(parties, "parties is required");
        Objects.requireNonNull(threadCount, "threadCount is required");
    }

    public static CyclicBarrierCompletionEx of(Integer parties, Integer threadCount) {
        return new CyclicBarrierCompletionEx(parties, threadCount, new AtomicInteger(0));
    }

    public int countTrips() {
        var cyclicBarrier = new CyclicBarrier(parties, updateCount::incrementAndGet);
        var es = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            es.execute(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        es.shutdown();

        try {
            es.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return updateCount.get();
    }
}
```

## Test Cases
```java
class CyclicBarrierCompletionExTest {

    @Test
    void givenCyclicBarrier_whenPartiesIs7WithThreadCount20_thenCountTripsIs2() {
        //Whenever the threadCount reaches 7 (the 7th thread trips the barrier), CyclicBarrier increments the AtomicInteger updateCount
        //So when
        //threadCount : 0 - 6, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 0
        //threadCount : 7 - 13, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 1
        //threadCount : 14 - 20, AtomicInteger updateCount inside CyclicBarrierCompletionEx is 1
        var ex = CyclicBarrierCompletionEx.of(7, 20);
        assertThat(ex.countTrips()).isEqualTo(2);
    }

    @Test
    void givenCyclicBarrier_whenPartiesIs5WithThreadCount20_thenCountTripsIs4() {
        //Whenever the threadCount reaches 5 (the 5th thread trips the barrier), CyclicBarrier increments the AtomicInteger updateCount
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
```