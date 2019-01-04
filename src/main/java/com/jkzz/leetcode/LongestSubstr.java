package com.jkzz.leetcode;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

/**
 * @program: jkzzDemo
 * @description: 不含重复字符的最长子字符串长度
 * @author: 周振
 * @create: 2019-01-01 21:46
 **/
public class LongestSubstr {
    public static void main(String[] args) {
     LongestSubstr lsStr=new LongestSubstr();
     System.out.println(lsStr.lengthOfLongestSubstring("123345678390"));
        boolean blank = StringUtils.isBlank("\n 1");
        System.out.println(blank);

    }
    public int lengthOfLongestSubstring(String s) {
        if (s.length() == 0) return 0;
        HashMap<Character, Integer> map = new HashMap<>();
        int max = 0;
        for (int i = 0, j = 0; i < s.length(); ++i) {
            if (map.containsKey(s.charAt(i))) {
                j = Math.max(j, map.get(s.charAt(i)) + 1);
            }
             map.put(s.charAt(i), i);
            max = Math.max(max, i - j + 1);
        }
        return max;
    }
}
