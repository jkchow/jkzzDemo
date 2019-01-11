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
        return M[num / 1000] + C[(num % 1000) / 100] + X[(num % 100) / 10] + I[num % 10];
    }

    //性能更快
    public static String intToRoman1(int num) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] strs = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                sb.append(strs[i]);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        long l = System.nanoTime();
        String s = intToRoman(1111);
        long l2 = System.nanoTime();
        System.out.println(l2-l);

        long l3 = System.nanoTime();
        String s1 = intToRoman1(1971);
        long l4 = System.nanoTime();
        System.out.println(l4-l3);
    }
}
