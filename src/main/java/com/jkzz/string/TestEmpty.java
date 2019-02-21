package com.jkzz.string;

import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;

public class TestEmpty {
    public static void main(String[] args) {
        String a = new String();
        String b = "  ";
        String c = null;
        String d = "qweqwrwRRRRR2342355FFee";
        //upAndDown(d);
         testString(a);
         testString(b);
         testString(c);
    }

    private static void testString(String str){

        boolean blank = StringUtils.isBlank(str);System.out.println(blank);
    }

    private static void  upAndDown(String str){
        char[] ch=str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            System.out.println(ch[i]);
            if (ch[i] >= 'A' && ch[i] <= 'Z') {
                ch[i] = (char)(ch[i]+32);
            } else if (ch[i] >= 'a' && ch[i] <= 'z') {
                ch[i] = (char)(ch[i]-32);
            }
            System.out.println(ch[i]);
        }
        System.out.println(String.copyValueOf(ch));
    }


}
