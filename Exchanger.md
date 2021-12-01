# Exchanger

Exchanger allows two threads to exchange objects at a rendezvous point, and can be useful in pipeline designs.
Each thread presents some object on entry to the `exchange(T t)` method and receives the object presented by other thread on return.

When invoked `exchange(T t)` waits for the other thread in the pair to call it as well. 
At this point, the second thread find the first thread is waiting with its object. The thread exchanges the objects
they are holding and signals the exchange, and now they can return.

As an example, consider the classical producer-consumer problem (two entities share a channel).

### The Producer

```java
record Producer(Exchanger<String> exchanger) implements Runnable{

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
```

### The Consumer
```java
record Consumer(Exchanger<String> exchanger) implements Runnable {

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
```

### The test case

```java
class ProducerConsumerExchangerTest {

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
```

### The output
```shell
Produced: 0
Consumed:0
Produced: 01
Consumed:01
Produced: 12
Consumed:12
Produced: 23
Consumed:23
Produced: 34
Consumed:34
```
