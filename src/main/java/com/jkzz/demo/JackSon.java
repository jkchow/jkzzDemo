package com.jkzz.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Map;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-04 17:42
 **/
public class JackSon {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Friend friend = new Friend("yitina", 25);
        //写成字符串
        String text = mapper.writeValueAsString(friend);
        System.out.println(text);
        //写入文件
        mapper.writeValue(new File("friend.text"), friend);
        // 从字符串中读取
        Friend newFriend = mapper.readValue(text, Friend.class);
        System.out.println(newFriend);
        // 从文件中读取
        newFriend = mapper.readValue(new File("friend.text"), Friend.class);
        System.out.println(newFriend);
    }
}