package com.jkzz.sort;

import java.util.Arrays;

/**
 * @program: jkzzDemo
 * @description: 插入排序
 * @author: 周振
 * @create: 2018-12-10 18:53
 **/
public class InsertSort {


    public static int[] sort1(int[] ary) {
        for (int i = 1; i < ary.length; i++) {
            int temp = ary[i];
            int j;
            for (j = i - 1; j >= 0 && temp < ary[j]; j--) {
                ary[j + 1] = ary[j];
            }
            ary[j + 1] = temp;
        }
        return ary;
    }

    public static int[] insertSort(int[] ary) {
        for (int i = 1; i < ary.length; i++) {
            int temp = ary[i];
            int j;
            for (j = i - 1; j >= 0; j--) {
                if (temp < ary[j]) {
                    ary[j + 1] = ary[j];
                } else
                    break; //找到插入位置
            }
            ary[j + 1] = temp;
        }

        return ary;
    }

    public int[] sort(int[] source) {
        int[] arr = Arrays.copyOf(source, source.length);
        //从下标为1的元素开始选择合适位置插入，因为下标为0的只有一个元素，默认是有序的
        for (int i = 1; i < arr.length; i++) {
            //记录要插入的数据
            int tmp = arr[i];
            //从已经排序的序列最右边开始比较，找到比其小的数
            int j = i;
            while (j > 0 && tmp < arr[j - 1]) {
                arr[j] = arr[j - 1];
                j--;
            }
            if (j != i) {//存在比其更小的数插入
                arr[j] = tmp;
            }

        }
        return arr;
    }

    public static void main(String[] args) {
        int[] arr = {7, 3};
        int[] sort = new InsertSort().sort(arr);
        Arrays.sort(arr);
        for (int i : sort) {
            System.out.println(i);
        }
        for (int i : arr) {
            // System.out.println(i);
        }
    }

}
