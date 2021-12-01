package com.umar.apps.concurrent.condition;

import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public record ReentrantLockWithCondition(
        Stack<String> stack
        ,ReentrantLock lock
        ,Condition stackEmptyCondition
        ,Condition stackFullCondition) {

    private static final int CAPACITY = 5;

    public ReentrantLockWithCondition {
        Objects.requireNonNull(stack, "stack is required");
        Objects.requireNonNull(lock, "lock is required");
        stackEmptyCondition = lock.newCondition();
        stackFullCondition = lock.newCondition();
    }

    public static ReentrantLockWithCondition of() {
        return new ReentrantLockWithCondition(new Stack<>(), new ReentrantLock(), null, null);
    }

    public void push(String item) {
        try{
            lock.lock();
            if(stack.size() == CAPACITY) {
                System.out.printf("%s waiting. Stack Full.\n ", Thread.currentThread().getName());
                stackFullCondition.await();
            }
            System.out.printf("Pushing Item:%s\n", item);
            stack.push(item);
            stackEmptyCondition.signalAll();
        }catch (InterruptedException ex) {
            ex.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public String pop() {
        try {
            lock.lock();
            if(stack.size() == 0) {
                System.out.printf("%s waiting. Stack Empty.\n ", Thread.currentThread().getName());
                stackEmptyCondition.await();
            }
            return stack.pop();
        } catch (InterruptedException ex){
            ex.printStackTrace();
        } finally {
            stackFullCondition.signalAll();
            lock.unlock();
        }
        throw new RuntimeException();
    }
}
