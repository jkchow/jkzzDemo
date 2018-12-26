package com.jkzz.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2018-12-26 10:15
 **/


public class TestSort {

    public static void main(String[] args) {

        List<Person> list = new ArrayList<Person>();

        Person p1 = new Person("d", 55);
        Person p0 = new Person("d", 5);
        Person p2 = new Person("c", 18);
        Person p3 = new Person("a", 37);

        list.add(p0);
        list.add(p1);
        list.add(p2);
        list.add(p3);

        //list.forEach(u->System.out.println(u));

        System.out.println(list);
        list.stream().sorted((u1, u2) -> (u2.getAge()).compareTo(u1.getAge())).collect(Collectors.toList());
                //
       System.out.println(list);
        //list.stream().forEach(u -> System.out.println(u));


        List<Integer> nums = Arrays.asList(1,null,3,4,null,6);
        long count = nums.stream().filter(num -> num != null).count();
        System.out.println("数量");
        System.out.println(count);
        int sum = nums.stream().filter(num -> num != null).distinct().mapToInt(num -> num * 2).peek(System.out::println).skip(2).limit(4).sum();
        System.out.println(sum);
        nums.stream().forEach(u -> System.out.println(u));
    }


}

class Person {
    public String name;
    public Integer age;

    public Person(String name, Integer age) {
        super();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + "]";
    }
}

