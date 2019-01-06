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

    private static Domain domain = new Domain();

    private static Map<String, String> domainMap = new HashMap<>();

    private Domain() {
        domainMap.put("isTrue", "true");
    }

    public static Domain getInstance() {
        return domain;
    }
}
