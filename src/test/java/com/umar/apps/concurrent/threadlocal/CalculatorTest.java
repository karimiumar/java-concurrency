package com.umar.apps.concurrent.threadlocal;

public class CalculatorTest extends Calculator implements Runnable{

    @Override
    protected Object doLocalCalculate(Object param) {
        System.out.printf("Doing calculation of %s in thread %s\n", param, Thread.currentThread());
        return param;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            Integer p = i % 5;
            calculate(p);
        }
    }

    public static void main(String[] args) {
        int nThreads = 5;
        for (int i = 0; i < nThreads; i++) {
            new Thread(new CalculatorTest()).start();
        }
    }
}
