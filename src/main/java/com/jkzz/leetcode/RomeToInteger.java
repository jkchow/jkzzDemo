package com.jkzz.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-18 21:51
 **/
public class RomeToInteger {
    public int romanToInt(String s) {
        int sum = 0;
        if (s.indexOf("IV") != -1) {
            sum -= 2;
        }
        if (s.indexOf("IX") != -1) {
            sum -= 2;
        }
        if (s.indexOf("XL") != -1) {
            sum -= 20;
        }
        if (s.indexOf("XC") != -1) {
            sum -= 20;
        }
        if (s.indexOf("CD") != -1) {
            sum -= 200;
        }
        if (s.indexOf("CM") != -1) {
            sum -= 200;
        }
        char c[] = s.toCharArray();
        int count = 0;
        for (; count <= s.length() - 1; count++) {
            if (c[count] == 'M') sum += 1000;
            if (c[count] == 'D') sum += 500;
            if (c[count] == 'C') sum += 100;
            if (c[count] == 'L') sum += 50;
            if (c[count] == 'X') sum += 10;
            if (c[count] == 'V') sum += 5;
            if (c[count] == 'I') sum += 1;

        }

        return sum;
    }

    public int romanToInt1(String s) {
        Map<Character, Integer> map = new HashMap();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);

        char[] sc = s.toCharArray();
        int total = map.get(sc[0]);
        int pre = map.get(sc[0]);
        for(int i = 1; i < sc.length; i++) {
            int cur = map.get(sc[i]);
            if(cur <= pre) {total += cur;} else {total = total + cur - 2 * pre;}
            pre = cur;
        }
        return total;
    }

}
