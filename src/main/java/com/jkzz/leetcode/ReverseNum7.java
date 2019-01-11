package com.jkzz.leetcode;

import java.util.function.Function;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-11 10:24
 **/
public class ReverseNum7 {
    public int reverse(int x) {
        int result = 0;
        while (x != 0) {
            int tail = x % 10;
            int newResult = result * 10 + tail;
            if ((newResult - tail) / 10 != result) {
                return 0;
            }
            result = newResult;
            x = x / 10;
        }
        return result;
    }

    public int reverse1(int x) {
        int sum = 0;
        while (x != 0) {
            if (sum > Integer.MAX_VALUE / 10 || sum == Integer.MAX_VALUE / 10 && x % 10 >= 7
                    || sum < Integer.MIN_VALUE / 10 || sum == Integer.MIN_VALUE / 10 && x % 10 <= -8) {
                return 0;
            }
            sum = 10 * sum + x % 10;
            x /= 10;
        }
        return sum;
    }

    public static void main(String[] args) {
        ReverseNum7 rs7 = new ReverseNum7();
        int reverse = rs7.reverse(Integer.MAX_VALUE);
        System.out.println(reverse);
        Function helloFunction = s -> "Hello " + s;
    }
}
