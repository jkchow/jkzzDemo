package com.jkzz.demo;

import java.util.HashSet;
import java.util.Set;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-04 17:43
 **/
public class Friend {
    private String nickname;
    private int age;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Friend(String nickname, int age) {
        this.nickname = nickname;
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return nickname.hashCode()+age;
    }

    public Friend() {
    }

    @Override
    public String toString() {
        return "Friend->{" +
                "nickname='" + nickname + '\'' +
                ", age=" + age +
                '}';
    }


    public static void main(String[] args) {
        Set<Friend> set = new HashSet<Friend>();
        Friend p1 = new Friend("唐僧", 25);
        Friend p2 = new Friend("孙悟空", 26);
        Friend p3 = new Friend("猪八戒", 27);
        set.add(p1);
        set.add(p2);
        set.add(p3);
        System.out.println("总共有:" + set.size() + " 个元素!"); //结果：总共有:3 个元素!
        for (Friend person : set) {
            System.out.println(person);
        }
        System.out.println("______");
        p3.setAge(2); //修改p3的年龄,此时p3元素对应的hashcode值发生改变
        set.remove(p3); //此时remove不掉，造成内存泄漏
        //set.add(p3); //重新添加，居然添加成功
        System.out.println("总共有:" + set.size() + " 个元素!"); //结果：总共有:4 个元素!
        for (Friend person : set) {
            System.out.println(person);
        }
    }


}
