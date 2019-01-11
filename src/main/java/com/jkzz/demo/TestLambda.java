package com.jkzz.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

/**
 * @program: java 10 demo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-11 11:44
 **/
public class TestLambda {
    public static void main(String[] args) {
        Function helloFunction = s -> "Hello " + s;
        List strings = new ArrayList<>();
        strings.add(helloFunction.apply("World"));
        //System.out.println(strings);
        Consumer printer = (x) -> System.out.println(x + "123");
        strings.forEach(printer::accept);
        //Set<String> set1 = Set.of("a","b", "c");
        String join = String.join("a", "b", "c");
        System.out.println(join);
        System.out.println();
        String join1 = String.join("c", "d", "fg");
        System.out.println(join1);
        List<String> myList = Arrays.asList("a1", "a2", "b1", "c2tttx", "c1yy");
        System.out.println(myList);
        //过滤c开头的 大写 排序 打印
        myList.stream().filter(s -> s.startsWith("c")).map(String::toUpperCase).sorted().forEach(s -> System.out.println(s));
        myList.stream().filter(s -> s.startsWith("c")).map(String::toUpperCase).sorted().forEach(System.out::println);


        IntStream.range(4, 8).forEach(System.out::println);//.average()
        System.out.println();
        //为每一元素 计算 然后遍历
        Arrays.stream(new int[]{1, 2, 3}).map(n -> 2 * n + 1).forEach(System.out::println);
        //为每一元素 计算 然后求平均值
        String s = Arrays.stream(new int[]{1, 2, 3}).map(n -> 2 * n + 1).average().toString();
        System.out.println(s);


    }
}
