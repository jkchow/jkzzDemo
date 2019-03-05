package com.jkzz.demo;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-02-28 16:34
 **/
public class TestVolatile {
    volatile int i;
    public final int incrementAndGet(int h, int s) {
        for (;;) {
            h=i;                 //A线程叫AH，B线程描述为BH       1
            s = i +1;         // A线程叫AS，B线程描述为BS        2
            if(h==i){            // 比较内存值和预期值               3
                i=s;            // 如果相同，赋值，成功CAS            4
                return s;
            }
        }
    }

    public static void main(String[] args) {

    }
    static void test(){
        final int b;
       // System.out.println(b+2);

    }
}
