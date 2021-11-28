## Thread

In Java, `java.lang.Thread` class is used to represent an application or JVM thread. Code is always being executed in the context of some `Thread` class.

### Thread Communication

The most obvious way to communicate between threads is for one thread to to directly call a method on another `Thread` object.

### Thread coordination methods:

|Thread Method  | Description                          |
|---------------|--------------------------------------|
|`start()`      | Start a `Thread` instance and execute its `run()` method.|
|`join()`       | Block until one thread exits.|
|`interrupt()`  | Interrupt the other thread. Of the thread is blocked in a method that responds to interrupts, an `InterruptedException` will be thrown in the other thread, otherwise the interrupt status is set. |
|`stop()`,`suspend()`,`resume()`, `destroy()`| These methods are all deprecated and should not be used. Instead use `interrupt()` or a `volatile` flag to indicate to a thread what it should do.|
|`wait()`/`notify()`| The wait/notify idiom is appropriate whenever one thread needs to signal to another that a condition has been met.

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
