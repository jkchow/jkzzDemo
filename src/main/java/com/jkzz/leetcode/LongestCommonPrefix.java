package com.jkzz.leetcode;

import java.util.Arrays;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-27 21:44
 **/
public class LongestCommonPrefix {
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        String pre = strs[0];
        int i = 1;
        while (i < strs.length) {
            while (strs[i].indexOf(pre) != 0)
                pre = pre.substring(0, pre.length() - 1);
            i++;
        }
        return pre;
    }

    public static String longestCommonPrefix1(String[] strs) {
        if (strs == null || strs.length == 0) return "";
        String pre = strs[0];
        int i = 1;
        while (i < strs.length) {
            while (strs[i].indexOf(pre) != 0)
                pre = pre.substring(0, pre.length() - 1);
            /**
             * If the first cycle is completed, aimPrefix.length() == 0 is true
             * it's mean there is no longest prefix surely
             */
            if (pre.length() == 0) {
                return "";
            } else {
                i++;
            }
        }
        return pre;
    }

    public String longestCommonPrefix3(String[] strs) {
        StringBuilder result = new StringBuilder();

        if (strs != null && strs.length > 0) {

            Arrays.sort(strs);

            char[] a = strs[0].toCharArray();
            char[] b = strs[strs.length - 1].toCharArray();

            for (int i = 0; i < a.length; i++) {
                if (b.length > i && b[i] == a[i]) {
                    result.append(b[i]);
                } else {
                    return result.toString();
                }
            }
            return result.toString();
        }
        return result.toString();
    }


    public static void main(String[] args) {
        String[] abs = {"12", "342"};
        longestCommonPrefix(abs);
    }
}
