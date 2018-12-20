package com.jkzz.leetcode;

/**
 * @program: jkzzDemo
 * @description: 反转元音
 * @author: 周振
 * @create: 2018-12-20 13:33
 **/
public class ReverseVowel {
    public static void main(String[] args) {
        System.out.println(ReverseVowel.reverseVowels4("ooeabec"));
    }

    public static String reverseVowels3(String s) {
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

    /**
     *
     */
    public static String reverseVowels4(String s) {
        if (s == null || s.trim().length() == 0) {
            return s;
        }
        StringBuffer sb = new StringBuffer();
        String str = "AEIOUaeiou";
        int j = s.length() - 1;
        for (int i = 0; i < s.length(); i++) {
            if (str.indexOf(s.charAt(i)) != -1) {//是元音
                while (j >= 0 && str.indexOf(s.charAt(j)) == -1) {//不是元音
                    j--;
                }
                sb.append(s.charAt(j));
                j--;
            } else {
                sb.append(s.charAt(i));
            }

        }
        return sb.toString();

    }

    public String reverseVowels(String s) {
        if (s == null || s.length() == 0) return s;
        String vowels = "aeiouAEIOU";
        char[] chars = s.toCharArray();
        int start = 0;
        int end = s.length() - 1;
        while (start < end) {
            while (start < end && !vowels.contains(chars[start] + "")) {
                start++;
            }
            while (start < end && !vowels.contains(chars[end] + "")) {
                end--;
            }
            char temp = chars[start];
            chars[start] = chars[end];
            chars[end] = temp;
            start++;
            end--;
        }
        return new String(chars);
    }
    public String reverseVowels1(String s) {
        if(s==null || s.length()==0)
            return s;
        int i=0, j=s.length()-1;
        char[] str = s.toCharArray();
        while(i<j){
            while(i<j && !isVowel(str[i]))
                i++;
            while(i<j && !isVowel(str[j]))
                j--;

            char temp = str[i];
            str[i]=str[j];
            str[j]=temp;
            i++;
            j--;
        }
        return new String(str);
    }

    private boolean isVowel(char ch){
        ch = Character.toLowerCase(ch);
        return ch == 'a' || ch == 'e' || ch == 'i' ||ch == 'o' ||ch == 'u';
    }
}
