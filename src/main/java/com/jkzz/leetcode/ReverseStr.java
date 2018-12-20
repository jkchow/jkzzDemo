package com.jkzz.leetcode;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2018-12-20 21:00
 **/
public class ReverseStr {
    public static String reverseString(String s){
        char[] word=s.toCharArray();
        int i=0;
        int j=s.length()-1;
        while(i<j){
            char temp=word[i];
            word[i]=word[j];
            word[j]=temp;
            i++;
            j++;
        }
        return new String(word);
    }
}
