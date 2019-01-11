package com.jkzz.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @program: java 10 demo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-11 11:44
 **/
public class Testjdk10 {
    public static void main(String[] args) {
        Function helloFunction = s -> "Hello " + s;
        List strings = new ArrayList<>();
        strings.add(helloFunction.apply("World"));
        //System.out.println(strings);
        Consumer printer = (x)->System.out.println(x+"123");


        strings.forEach(printer::accept);
    }
}
