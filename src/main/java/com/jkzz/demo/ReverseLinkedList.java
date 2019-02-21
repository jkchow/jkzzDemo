package com.jkzz.demo;

/**
 * @program: jkzzDemo
 * @description: 类描述
 * @author: 周振
 * @create: 2019-02-21 10:08
 **/
public class ReverseLinkedList {

    public static void main(String[] args) {
        Node head = new Node(3);
        head.next = new Node(5);
        Node temp =head.next;
        temp.next = new Node(1);
        temp=temp.next;
        temp.next = new Node(9);
        temp=head;
        //逆序前 输出列表
        while (temp!=null){
            System.out.println(temp.date);
            temp=temp.next;
        }


    }
    private static void reverseLinkedList(Node node){

    }
}



class Node {
    int date;
    Node next;

    Node(int date) {
        this.date = date;
    }
}