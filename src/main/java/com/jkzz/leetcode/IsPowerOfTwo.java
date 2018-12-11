package com.jkzz.leetcode;

/**
 * @program: jkzzDemo
 * @description:  判断是否为2的指数幂
 * @author: 周振
 * @create: 2018-12-11 21:28
 **/
public class IsPowerOfTwo {
    public boolean isPowerOfTwo(int n){
      //  return  (n&-n)==n;
        // return ((n&(n-1))==0);
        if (n == 0) return false;
        while (n%2 == 0) n/=2;
        return n == 1;
    }
    public static void main(String[] args) {
     System.out.println(new IsPowerOfTwo().isPowerOfTwo(4));
    }
}
