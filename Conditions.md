# Condition

`Condition` factors out the `Object` monitor methods (`wait()`, `notify()` and `notifyAll()`) into distinct objects to give the effect of having multiple wait-sets per object, by combining them with the use of arbitrary Lock implementations. Where a `Lock` replaces the use of `synchronized` methods and statements, a `Condition` replaces the use of the `Object` monitor methods.

Conditions (also known as condition queues or condition variables) provide a means for one thread to suspend execution (to "wait") until notified by another thread that some state condition may now be true. Because access to this shared state information occurs in different threads, it must be protected, so a lock of some form is associated with the condition. The key property that waiting for a condition provides is that it atomically releases the associated lock and suspends the current thread, just like `Object.wait()`.

A `Condition` instance is intrinsically bound to a lock. To obtain a `Condition` instance for a particular `Lock` instance use its `newCondition()` method. 


###  An example
As an example, suppose we have a bounded buffer which supports `put()` and `take()` methods. If a `take()` is attempted on an empty buffer, then the thread will block until an item becomes available; if a `put()` is attempted on a full buffer, then the thread will block until a space becomes available. We would like to keep waiting put threads and take threads in separate wait-sets so that we can use the optimization of only notifying a single thread at a time when items or spaces become available in the buffer. This can be achieved using two `Condition` instances. 

```java
class BoundedBuffer {
   final Lock lock = new ReentrantLock();
   final Condition notFull  = lock.newCondition(); 
   final Condition notEmpty = lock.newCondition(); 

   final Object[] items = new Object[100];
   int putptr, takeptr, count;

   public void put(Object x) throws InterruptedException {
     lock.lock();
     try {
       while (count == items.length) {
           notFull.await();
       }
       items[putptr] = x;
       if (++putptr == items.length) {
           putptr = 0;
       }
       ++count;
       notEmpty.signal();
     } finally {
       lock.unlock();
     }
   }

   public Object take() throws InterruptedException {
     lock.lock();
     try {
       while (count == 0) {
           notEmpty.await();
       }
       Object x = items[takeptr];
       if (++takeptr == items.length) takeptr = 0;
       --count;
       notFull.signal();
       return x;
     } finally {
       lock.unlock();
     }
   }
 }
```

Note: (The `ArrayBlockingQueue` class provides the above functionality, so there is no reason to implement this sample usage class.)

A `Condition` implementation can provide behavior and semantics that is different from that of the `Object` monitor methods, such as guaranteed ordering for notifications, or not requiring a lock to be held when performing notifications. If an implementation provides such specialized semantics then the implementation must document those semantics. 