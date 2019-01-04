package com.jkzz.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
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
        test1();
    }

    static void test1() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        map.put("age", 25);
        map.put("name", "yitian");
        map.put("interests", new String[]{"pc games", "music"});
        System.out.println(map);
        String text = mapper.writeValueAsString(map);
        System.out.println(text);
        Map map1 = mapper.readValue(text, Map.class);
        System.out.println(map1);
        System.out.println(map1.get("interests"));
        Map<String, Object> map2 = mapper.readValue(text, new TypeReference<Map<String, Object>>() {});
        System.out.println(map2);
        map2.get("interests");
       /*for (Object s:interests) {
           System.out.println(s.toString());
        }*/
    }
}