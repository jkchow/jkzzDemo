package com.jkzz.leetcode;

/**
 * @program: jkzzDemo
 * @description: 反转元音
 * @author: 周振
 * @create: 2018-12-20 13:33
 **/
public class ReverseVowel {
    public String reverseVowels3(String s) {
        if (s == null || s.trim().length() <= 1) {
            return s;
        }
        char[] arr = s.toCharArray();
        int i = 0, j = s.length() - 1;
        while (i < j) {
            if (isVowels(arr[i]) && isVowels(arr[j])) {
                char temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
            if (!isVowels(arr[i])) {
                i++;
            }
            if (!isVowels(arr[j])) {
                j--;
            }
        }
        return new String(arr);
    }

    public static boolean isVowels(char ch) {
        if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
            return true;
        }
        if (ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U') {
            return true;
        }
        return false;
    }
}
