package com.jkzz.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2018-12-28 14:41
 **/
public class TestFlatMap {
    public static void main(String[] args) {
     flatMapString();
    }
    public static void flatMapString() {
        List<PersonModel> data = Data.getData();
        //返回类型不一样
        List<String> collect = data.stream()
                .flatMap(person -> Arrays.stream(person.getName().split(" "))).collect(toList());

        List<Stream<String>> collect1 = data.stream()
                .map(person -> Arrays.stream(person.getName().split(" "))).collect(toList());

        //用map实现
        List<String> collect2 = data.stream()
                .map(person -> person.getName().split(" "))
                .flatMap(Arrays::stream).collect(toList());
        //另一种方式
        List<String> collect3 = data.stream()
                .map(person -> person.getName().split(" "))
                .flatMap(str -> Arrays.asList(str).stream()).collect(toList());
        System.out.println(collect);
        System.out.println(collect1);
        System.out.println(collect2);
        System.out.println(collect3);
    }


}
