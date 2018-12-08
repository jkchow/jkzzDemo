package com.jkzz.arraylist;

import java.util.ArrayList;

/**
 * arrayList 优化  add优化
 */
public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<Integer> list1 = new ArrayList<>();
        int addCount = 1000000; // 这个值不要太小，否则效果不明显
        // 没有优化
        long begin1 = System.currentTimeMillis();
        for (int i = 0; i < addCount; i++) {
            list1.add(i);
        }
        long end1 = System.currentTimeMillis();
        long cost1 = end1 - begin1;

        ArrayList<Integer> list2 = new ArrayList<>();
        // 有优化
        list2.ensureCapacity(addCount);
        long begin2 = System.currentTimeMillis();
        for (int i = 0; i < addCount; i++) {
            list2.add(i);
        }
        long end2 = System.currentTimeMillis();
        long cost2 = end2 - begin2;

        System.err.println("大量 add 操作没有优化前的时间花费：" + cost1);
        System.err.println("大量 add 操作优化后的时间花费：" + cost2);

    }

}
