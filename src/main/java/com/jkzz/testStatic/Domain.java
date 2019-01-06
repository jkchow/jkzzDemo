package com.jkzz.testStatic;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-06 16:04
 **/
public class Domain {
    private static Map<String, String> domainMap = new HashMap<>();
    private static Domain domain = new Domain();



    private Domain() {
        domainMap.put("isTrue", "true");
    }

    public static Domain getInstance() {
        return domain;
    }
}

class test1 {
    public static void main(String[] args) {
        String s = Domain.getInstance().toString();
        System.out.println(s);
    }
}