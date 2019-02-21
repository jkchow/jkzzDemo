package com.jkzz.demo;

/**
 * @program: 反转链表
 * @description: 类描述
 * @author: 周振
 * @create: 2019-02-21 10:08
 **/
public class ReverseLinkedList {

    public static void main(String[] args) {
        Node head = new Node(3);
        head.next = new Node(5);
        Node temp = head.next;
        temp.next = new Node(1);
        temp = temp.next;
        temp.next = new Node(9);
        temp = head;
        //逆序前 输出列表
        while (temp != null) {
            System.out.println(temp.date);
            temp = temp.next;
        }
        // Node node = reverseLinkedList(head);
        Node node = reverseList(head);
        temp = node;
        //逆序前 输出列表
        System.out.println("--------------");
        while (temp != null) {
            System.out.println(temp.date);
            temp = temp.next;
        }
    }

    /**
     * 使用递归
     * @param node
     * @return
     */
    private static Node reverseLinkedList(Node node) {
        if (node == null || node.next == null) {
            return node;
        }
        Node p1 = node;
        Node p2 = node.next;
        Node p3 = null;
        while (p2 != null) {
            p3 = p2.next;
            p2.next = p1;
            p1 = p2;
            p2 = p3;
        }
        node.next = null;
        return p1;
    }
    static int i=1;
    /**
     * 递归方法
     * @param node
     * @return
     */
    public static Node reverseList(Node node){

        if (node ==null||node.next==null) {
            return node;
        }

        Node pNext=node.next;
        Node temp=reverseList(pNext);

        pNext.next = node;
        node.next=null;
        return temp;
    }




}

class Node {
    int date;
    Node next;

    Node(int date) {
        this.date = date;
    }
}