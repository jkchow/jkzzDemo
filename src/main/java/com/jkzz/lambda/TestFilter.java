package com.jkzz.lambda;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2018-12-27 14:08
 **/
public class TestFilter {

    public static void main(String[] args) {
        filterSex();
    }

    /**
     * 过滤所有男性
     */
    public static void filterSex() {
        //old
        List<PersonModel> data = Data.getData();
        List<PersonModel> temp = new ArrayList<>();
        for (PersonModel person : data) {
            if ("男".equals(person.getSex())) {
                temp.add(person);
            }
        }
        System.out.println(temp);
        //new
        List<PersonModel> collect = data.stream().filter(person -> "男".equals(person.getSex())).collect(toList());
        System.out.println(collect);
    }

    /**
     * 过滤所有年龄小于20的男性
     */
    public static void fiterSexAndAge() {
        List<PersonModel> data = Data.getData();

        //old
        List<PersonModel> temp = new ArrayList<>();
        for (PersonModel person : data) {
            if ("男".equals(person.getSex()) && person.getAge() < 20) {
                temp.add(person);
            }
        }

        //new 1
        List<PersonModel> collect = data
                .stream()
                .filter(person -> {
                    if ("男".equals(person.getSex()) && person.getAge() < 20) {
                        return true;
                    }
                    return false;
                })
                .collect(toList());
        //new 2
        List<PersonModel> collect1 = data
                .stream()
                .filter(person -> ("男".equals(person.getSex()) && person.getAge() < 20))
                .collect(toList());

    }
}
