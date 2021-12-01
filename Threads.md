## Thread
In Java, `java.lang.Thread` class is used to represent an application or JVM thread. Code is always being executed in the context of some `Thread` class.

### Thread Communication
The most obvious way to communicate between threads is for one thread to to directly call a method on another `Thread` object.

### Thread coordination methods:
| Thread Method                                | Description                                                                                                                                                                                        |
|----------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `start()`                                    | Start a `Thread` instance and execute its `run()` method.                                                                                                                                          |
| `join()`                                     | Block until one thread exits.                                                                                                                                                                      |
| `interrupt()`                                | Interrupt the other thread. Of the thread is blocked in a method that responds to interrupts, an `InterruptedException` will be thrown in the other thread, otherwise the interrupt status is set. |
| `stop()`,`suspend()`,`resume()`, `destroy()` | These methods are all deprecated and should not be used. Instead use `interrupt()` or a `volatile` flag to indicate to a thread what it should do.                                                 |
| `wait()`/`notify()`                          | The wait/notify idiom is appropriate whenever one thread needs to signal to another that a condition has been met.                                                                                 |

### Uncaught Exception handlers

Threads can specify an `UncaughtExceptionHandler` that will receive notification of any uncaught exception that cause a thread to abruptly terminate.

### Deadlock
A deadlock occurs when more than one thread, each waiting for a resource held by another, such that a cycle of resources and acquiring threads is formed.

### Livelock
Livelock occurs when threads spend all of their time negotiating access to a resource or detecting and avoiding a deadlock such that no thread actually makes progress.

### Starvation
Starvation occurs when threads hold a lock for long periods such that some other threads "starve" without making progress.

### Important points
1. Always call `wait()`, `notify()` and `notifyAll()` inside a `synchronized` lock or an `IllegalMonitorStateException` will be thrown.
2. Always `wait()` inside a loop that checks the condition being waited on.
3. Always ensure that you satisfy the waiting condition before calling `notify()` or `notifyAll()`.

### Java Concurrency - Synchronizers
The `java.util.concurrent` package introduced in Jdk 5 contains several classes that help manage a set of threads that collaborate with each other. Some of these include:

* [CyclicBarrier](CyclicBarrier.md)
* [Phaser](Phaser.md)
* [CountDownLatch](CountDownLatch.md)
* [Exchanger](Exchanger.md)
* [Semaphore](Semaphore.md)

### Threads Priority
In Java, a thread's priority is an integer in the range of 0-10. The larger the integer the higher the priority.
The thread scheduler uses this integer from each thread to determine which one should be allowed to execute. The `Thread` class defines three type of priorities:
1. Minimum Priority `MIN_PRIORITY = 1`
2. Normal Priority  `NORM_PRIORITY = 5` Default priority of a thread.
3. Maximum Priority `MAX_PRIORITY = 10`

### Thread Execution
The JVM supports a thread scheduling algorithm called fixed-priority pre-emptive scheduling. JVM serves the highest priority threads first.
In case two threads have same priority, the JVM will execute them in FIFO order.

The following scenarios can cause a different thread to run:
1. A thread with higher priority than the current thread becomes Runnable.
2. The current thread exits the runnable state or **yields** (temporarily pause and allow other threads).

### Changing a Thread's Priority
Use the `setPriority(int priority)` method to change the default priority of a thread while creating.

### Finding a Thread's Priority
Use the `int getPriority()` method to find the priority of a thread.

### How to Get the number of Threads in a Java process
1. Use a Grahical monitoring JVM tools like Java VisualVM
2. Use `Thread.activeCount()` method to know about the active threads running by an application. It uses `ThreadGroup` so the number of active threads returned are less than the one visible in Java VisualVM
3. To find the group use `Thread.currentThread().getThreadGroup().getName()`
4. Use `top -H -p l` command to display threads in Java process (-H to display Java threads) on Linux

### Why not to start a Thread in a Constructor
When the `this` reference escapes during construction, other threads may see that object in an improper and not fully-constructed state. This, in turn, can cause weird thread-safety complications.

Consider the example below where a thread escapes `this` reference even though the object is not completely available.

```java
class LoggerRunnable implements Runnable {

    public LoggerRunnable() {
        Thread thread = new Thread(this); // this escapes
        thread.start();
    }

    @Override
    public void run() {
        System.out.println("Started...");
    }
}
```