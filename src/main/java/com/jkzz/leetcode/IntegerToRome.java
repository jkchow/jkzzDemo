package com.jkzz.leetcode;

/**
 * @program: 数字转罗马数字
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-11 15:30
 **/
public class IntegerToRome {
    public static String intToRoman(int num) {
        String M[] = {"", "M", "MM", "MMM"};
        String C[] = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String X[] = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String I[] = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        return  M[num/1000] + C[(num%1000)/100] + X[(num%100)/10] + I[num%10];
    }
    public static void main(String[] args) {
        String s = intToRoman(1111);
        System.out.println(s);
    }
}
