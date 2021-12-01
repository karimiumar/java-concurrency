# Lock

The `java.util.concurrent.locks` package has a standard `Lock` interface. The `ReentrantLock` implementation duplicates the functionality of the `synchronized` keyword but also provides additional functionality such as obtaining information about the state of the lock, non-blocking `tryLock()`, and interruptible locking.

An example of using an explicit `ReentrantLock` instance:

```java
import java.util.concurrent.locks.ReentrantLock;

class Counter {
    private final Lock lock = new ReentrantLock();
    private int value = 0;
    
    private int increment() {
        lock.lock();
        try{
            return ++value;
        }finally {
            lock.unlock();
        }
    }
}
```
# ReadWriteLock

The `java.util.concurrent.locks` package also contains a `ReadWriteLock` interface and `ReentrantReadWriteLock` provide its implementation which is defined by a pair of locks for reading and writing, typically allowing multiple concurrent readers but only one writer.

An example of using an explicit `ReentrantReadWriteLock` to allow multiple concurrent readers:

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Statistic {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private int value;
    
    void increment(){
        lock.writeLock().lock();
        try{
            value++;
        }finally {
            lock.writeLock().unlock();
        }
    }
    
    int current() {
        lock.readLock().lock();
        try{
            return value;
        }finally {
            lock.readLock().unlock();
        }
    }
}
```

