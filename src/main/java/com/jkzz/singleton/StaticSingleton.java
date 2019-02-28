package com.jkzz.singleton;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-02-28 10:36
 **/
public class StaticSingleton {
    //内部类
    private static class MySingletonHandler {
        private static StaticSingleton instance = new StaticSingleton();
    }

    private StaticSingleton() {
    }

    public static StaticSingleton getInstance() {
        return MySingletonHandler.instance;
    }

}
