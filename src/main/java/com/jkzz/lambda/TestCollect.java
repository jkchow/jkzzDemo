package com.jkzz.lambda;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2018-12-28 14:46
 **/
public class TestCollect {

    public static void main(String[] args) {
     //toListTest();
        //toSetTest();
        toMapTest();
    }
    /**
     * toList
     */
    public static void toListTest() {
        List<PersonModel> data = Data.getData();
        System.out.println(data);
        List<String> collect = data.stream()
                .map(PersonModel::getName)
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    /**
     * toSet
     */
    public static void toSetTest() {
        List<PersonModel> data = Data.getData();
        Set<String> collect = data.stream()
                .map(PersonModel::getName)
                .collect(Collectors.toSet());
        System.out.println(collect);
    }

    /**
     * toMap
     */
    public static void toMapTest() {
        List<PersonModel> data = Data.getData();
        System.out.println(data);

        Map<String, Integer> collect = data.stream()
                .collect(
                        Collectors.toMap(PersonModel::getName, PersonModel::getAge)
                );
System.out.println(collect);
        Map<String, String> collect1 = data.stream()
                .collect(Collectors.toMap(per -> per.getName(), value -> {
                    //value.setAge(value.getAge()+1);
                    return value+"" ;
                }));
        System.out.println(collect1);
    }

    /**
     * 指定类型
     */
    public static void toTreeSetTest() {
        List<PersonModel> data = Data.getData();
        TreeSet<PersonModel> collect = data.stream()
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println(collect);
    }

    /**
     * 分组
     */
    public static void toGroupTest() {
        List<PersonModel> data = Data.getData();
        Map<Boolean, List<PersonModel>> collect = data.stream()
                .collect(Collectors.groupingBy(per -> "男".equals(per.getSex())));
        System.out.println(collect);
    }

    /**
     * 分隔
     */
    public static void toJoiningTest() {
        List<PersonModel> data = Data.getData();
        String collect = data.stream()
                .map(personModel -> personModel.getName())
                .collect(Collectors.joining(",", "{", "}"));
        System.out.println(collect);
    }

    /**
     * 自定义
     */
    public static void reduce() {
        List<String> collect = Stream.of("1", "2", "3").collect(
                Collectors.reducing(new ArrayList<String>(), x -> Arrays.asList(x), (y, z) -> {
                    y.addAll(z);
                    return y;
                }));
        System.out.println(collect);
    }


}
