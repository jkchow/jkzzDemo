package com.jkzz.leetcode;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-27 21:44
 **/
public class LongestCommonPrefix {
    public static String longestCommonPrefix(String[] strs) {
        if(strs == null || strs.length == 0)    return "";
        String pre = strs[0];
        int i = 1;
        while(i < strs.length){
            while(strs[i].indexOf(pre) != 0)
                pre = pre.substring(0,pre.length()-1);
            i++;
        }
        return pre;
    }
    public static String longestCommonPrefix1(String[] strs) {
        if(strs == null || strs.length == 0)    return "";
        String pre = strs[0];
        int i = 1;
        while(i < strs.length){
            while(strs[i].indexOf(pre) != 0)
                pre = pre.substring(0,pre.length()-1);
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
    public static void main(String[] args) {
        String[] abs={"12","342"};
        longestCommonPrefix(abs);
    }
}
