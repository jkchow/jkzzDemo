package com.jkzz.lambda;

/**
 * @program: jkzzDemo
 * @description: Runnable测试程序
 * @author: 周振
 * @create: 2018-12-28 17:37
 **/
public class RunnableTest {
    public static void main(String[] args) {
        //匿名内部类
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello runnable 1!");
            }
        };
        //lambda
        Runnable r2 = () -> System.out.println("hello runnable 1!");
        r1.run();
        r2.run();
    }
}
