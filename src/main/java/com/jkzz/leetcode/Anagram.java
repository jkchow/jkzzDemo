package com.jkzz.leetcode;

import java.lang.management.ManagementFactory;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2018-12-11 20:26
 **/
public class Anagram {
    public static void main(String[] args) throws Exception {
        //boolean anagram = new Anagram().isAnagram("abc", "cba");
        //System.out.println(anagram);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);
        // get pid
        String pid = name.split("@")[0];
        System.out.println("Pid is:" + pid);
        int i=100;
        StringBuilder a=new StringBuilder("sdfdsfkljshd");
        while (true) {
            //Thread.sleep(50l);
            //a.append("我是周振！");
            System.out.println(i++);
        }

    }

    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;

        char[] c1 = s.toCharArray();
        char[] c2 = t.toCharArray();
        int[] alpha = new int[26];

        for (int i = 0; i < c1.length; ++i) {
            alpha[c1[i] - 'a']++;
            alpha[c2[i] - 'a']--;
        }

        for (int num : alpha) {
            if (num != 0) return false;
        }
        return true;
    }
}
