package com.jkzz.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-01-04 17:42
 **/
public class JackSon {

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Friend friend = new Friend("yitina", 25);
        //写成字符串
        String text = mapper.writeValueAsString(friend);
        System.out.println(text);


    }
}