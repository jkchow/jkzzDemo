package com.jkzz.testStatic;

/**
 * @program: 测试注入静态变量
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-06 15:46
 **/
public class TestStatic {

    public static void main(String[] args) {
        String domainString = Domain.getInstance().toString();
        System.out.println(domainString);
    }

}
