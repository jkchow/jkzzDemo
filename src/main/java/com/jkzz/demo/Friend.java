package com.jkzz.demo;

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
    public String toString() {
        return "Friend->{" +
                "nickname='" + nickname + '\'' +
                ", age=" + age +
                '}';
    }
}
