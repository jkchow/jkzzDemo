package com.jkzz.demo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-03-01 17:30
 **/
public class SynchronizedDemo implements Runnable {
    private static volatile int count = 0;
    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new SynchronizedDemo());
            thread.start();
        }
        System.out.println((System.currentTimeMillis()-l));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("result: " + count);
        System.out.println("result:atomicInteger: " + atomicInteger);
    }

    @Override
    public void run() {
        synchronized (SynchronizedDemo.class){
            for (int i = 0; i < 1000000; i++) {
                count++;
                atomicInteger.getAndIncrement();
            }
        }

    }
}
